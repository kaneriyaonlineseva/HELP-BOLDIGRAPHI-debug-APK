package com.helpboldigraphi.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.helpboldigraphi.app.data.PreferencesRepository
import com.helpboldigraphi.app.domain.engine.SocialTemplatesCatalog
import com.helpboldigraphi.app.domain.engine.UnicodeStylizerEngine
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.SavedTextItem
import com.helpboldigraphi.app.domain.model.ScriptFilter
import com.helpboldigraphi.app.domain.model.ScriptKind
import com.helpboldigraphi.app.domain.model.SocialTemplate
import com.helpboldigraphi.app.domain.model.StyleCategory
import com.helpboldigraphi.app.domain.model.StylePreset
import com.helpboldigraphi.app.domain.model.TemplateCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class StudioUiState(
    val inputText: String = "HELP BOLDIGRAPHI\nआपका स्वागत है",
    val searchQuery: String = "",
    val scriptFilter: ScriptFilter = ScriptFilter.ALL,
    val selectedCategory: StyleCategory = StyleCategory.ALL,
    val selectedTemplateCategory: TemplateCategory = TemplateCategory.ALL,
    val previewFontSizeSp: Float = 21f,
    val isDarkMode: Boolean = true,
    val appLanguage: AppLanguage = AppLanguage.ENGLISH,
    val filteredPresets: List<StylePreset> = UnicodeStylizerEngine.allPresets,
    val filteredTemplates: List<SocialTemplate> = SocialTemplatesCatalog.templates,
    val favorites: List<SavedTextItem> = emptyList(),
    val history: List<SavedTextItem> = emptyList()
)

private data class FilterState(
    val input: String,
    val query: String,
    val script: ScriptFilter,
    val category: StyleCategory,
    val templateCategory: TemplateCategory
)

private data class PrefsSnapshot(
    val fontSize: Float,
    val isDark: Boolean,
    val language: AppLanguage,
    val favorites: List<SavedTextItem>,
    val history: List<SavedTextItem>
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PreferencesRepository(application)

    private val inputTextFlow = MutableStateFlow("HELP BOLDIGRAPHI\nआपका स्वागत है")
    private val searchQueryFlow = MutableStateFlow("")
    private val scriptFilterFlow = MutableStateFlow(ScriptFilter.ALL)
    private val categoryFlow = MutableStateFlow(StyleCategory.ALL)
    private val templateCategoryFlow = MutableStateFlow(TemplateCategory.ALL)

    init {
        viewModelScope.launch {
            val savedDraft = repository.draftText.first()
            if (savedDraft.isNotBlank()) {
                inputTextFlow.value = savedDraft
            }
        }
    }

    private val filterStateFlow = combine(
        inputTextFlow,
        searchQueryFlow,
        scriptFilterFlow,
        categoryFlow,
        templateCategoryFlow
    ) { input, query, script, category, tplCategory ->
        FilterState(input, query, script, category, tplCategory)
    }

    private val prefsSnapshotFlow = combine(
        repository.previewFontSize,
        repository.isDarkMode,
        repository.appLanguage,
        repository.favorites,
        repository.history
    ) { fontSize, isDark, language, favorites, history ->
        PrefsSnapshot(fontSize, isDark, language, favorites, history)
    }

    val uiState: StateFlow<StudioUiState> = combine(
        filterStateFlow,
        prefsSnapshotFlow
    ) { filters, prefs ->
        val q = filters.query.trim().lowercase()
        val filteredPresets = UnicodeStylizerEngine.allPresets.filter { preset ->
            val matchesScript = when (filters.script) {
                ScriptFilter.ALL -> true
                ScriptFilter.ENGLISH -> preset.script == ScriptKind.ENGLISH || preset.script == ScriptKind.BOTH
                ScriptFilter.HINDI -> preset.script == ScriptKind.HINDI || preset.script == ScriptKind.BOTH
            }
            val matchesCategory = filters.category == StyleCategory.ALL || preset.category == filters.category
            val matchesQuery = q.isEmpty() ||
                preset.name.lowercase().contains(q) ||
                preset.subtitle.lowercase().contains(q)
            matchesScript && matchesCategory && matchesQuery
        }

        val filteredTemplates = SocialTemplatesCatalog.templates.filter { tpl ->
            val matchesCat = filters.templateCategory == TemplateCategory.ALL ||
                tpl.category == filters.templateCategory
            val matchesScript = when (filters.script) {
                ScriptFilter.ALL -> true
                ScriptFilter.ENGLISH -> tpl.script == ScriptKind.ENGLISH || tpl.script == ScriptKind.BOTH
                ScriptFilter.HINDI -> tpl.script == ScriptKind.HINDI || tpl.script == ScriptKind.BOTH
            }
            matchesCat && matchesScript
        }

        StudioUiState(
            inputText = filters.input,
            searchQuery = filters.query,
            scriptFilter = filters.script,
            selectedCategory = filters.category,
            selectedTemplateCategory = filters.templateCategory,
            previewFontSizeSp = prefs.fontSize,
            isDarkMode = prefs.isDark,
            appLanguage = prefs.language,
            filteredPresets = filteredPresets,
            filteredTemplates = filteredTemplates,
            favorites = prefs.favorites,
            history = prefs.history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StudioUiState()
    )

    fun updateInputText(newText: String) {
        inputTextFlow.value = newText
        viewModelScope.launch { repository.setDraftText(newText) }
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun updateScriptFilter(filter: ScriptFilter) {
        scriptFilterFlow.value = filter
    }

    fun updateCategory(category: StyleCategory) {
        categoryFlow.value = category
    }

    fun updateTemplateCategory(category: TemplateCategory) {
        templateCategoryFlow.value = category
    }

    fun resetStudioFilters() {
        searchQueryFlow.value = ""
        scriptFilterFlow.value = ScriptFilter.ALL
        categoryFlow.value = StyleCategory.ALL
    }

    fun updateFontSize(sizeSp: Float) {
        viewModelScope.launch { repository.setPreviewFontSize(sizeSp) }
    }

    fun toggleTheme() {
        val next = !uiState.value.isDarkMode
        viewModelScope.launch { repository.setDarkMode(next) }
    }

    fun setAppLanguage(language: AppLanguage) {
        viewModelScope.launch { repository.setAppLanguage(language) }
    }

    fun toggleFavorite(preset: StylePreset, styledText: String) {
        val current = uiState.value.favorites
        val existing = current.find { it.styleId == preset.id && it.styledText == styledText }
        val updated = if (existing != null) {
            current.filterNot { it.id == existing.id }
        } else {
            listOf(
                SavedTextItem(
                    id = UUID.randomUUID().toString(),
                    sourceText = uiState.value.inputText,
                    styledText = styledText,
                    styleId = preset.id,
                    styleName = preset.name,
                    createdAt = System.currentTimeMillis(),
                    actionType = "favorited",
                    fontTypeName = preset.fontType.name
                )
            ) + current
        }
        viewModelScope.launch { repository.saveFavorites(updated) }
    }

    fun removeFavorite(id: String) {
        val updated = uiState.value.favorites.filterNot { it.id == id }
        viewModelScope.launch { repository.saveFavorites(updated) }
    }

    fun clearFavorites() {
        viewModelScope.launch { repository.saveFavorites(emptyList()) }
    }

    fun recordHistory(
        presetId: String,
        styleName: String,
        styledText: String,
        actionType: String,
        fontType: BundledFontType = BundledFontType.DEFAULT_SANS
    ) {
        val current = uiState.value.history
        val deduped = current.filterNot { it.styleId == presetId && it.styledText == styledText }
        val updated = listOf(
            SavedTextItem(
                id = UUID.randomUUID().toString(),
                sourceText = uiState.value.inputText,
                styledText = styledText,
                styleId = presetId,
                styleName = styleName,
                createdAt = System.currentTimeMillis(),
                actionType = actionType,
                fontTypeName = fontType.name
            )
        ) + deduped
        viewModelScope.launch { repository.saveHistory(updated.take(60)) }
    }

    fun removeHistoryItem(id: String) {
        val updated = uiState.value.history.filterNot { it.id == id }
        viewModelScope.launch { repository.saveHistory(updated) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.saveHistory(emptyList()) }
    }
}
