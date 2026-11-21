package com.helpboldigraphi.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.SavedTextItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "help_boldigraphi_prefs")

class PreferencesRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
        val DRAFT_TEXT = stringPreferencesKey("draft_text")
        val PREVIEW_FONT_SIZE = floatPreferencesKey("preview_font_size")
        val FAVORITES_JSON = stringPreferencesKey("favorites_json")
        val HISTORY_JSON = stringPreferencesKey("history_json")
    }

    private val safeData: Flow<Preferences> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }

    val isDarkMode: Flow<Boolean> = safeData.map { prefs ->
        prefs[Keys.DARK_MODE] ?: true
    }

    val appLanguage: Flow<AppLanguage> = safeData.map { prefs ->
        if (prefs[Keys.APP_LANGUAGE] == AppLanguage.HINDI.code) AppLanguage.HINDI else AppLanguage.ENGLISH
    }

    val draftText: Flow<String> = safeData.map { prefs ->
        prefs[Keys.DRAFT_TEXT] ?: "HELP BOLDIGRAPHI\nआपका स्वागत है"
    }

    val previewFontSize: Flow<Float> = safeData.map { prefs ->
        (prefs[Keys.PREVIEW_FONT_SIZE] ?: 21f).coerceIn(16f, 34f)
    }

    val favorites: Flow<List<SavedTextItem>> = safeData.map { prefs ->
        decodeList(prefs[Keys.FAVORITES_JSON])
    }

    val history: Flow<List<SavedTextItem>> = safeData.map { prefs ->
        decodeList(prefs[Keys.HISTORY_JSON])
    }

    suspend fun setDarkMode(dark: Boolean) {
        runCatching { context.dataStore.edit { it[Keys.DARK_MODE] = dark } }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        runCatching { context.dataStore.edit { it[Keys.APP_LANGUAGE] = language.code } }
    }

    suspend fun setDraftText(text: String) {
        runCatching { context.dataStore.edit { it[Keys.DRAFT_TEXT] = text } }
    }

    suspend fun setPreviewFontSize(sizeSp: Float) {
        runCatching { context.dataStore.edit { it[Keys.PREVIEW_FONT_SIZE] = sizeSp } }
    }

    suspend fun saveFavorites(items: List<SavedTextItem>) {
        runCatching { context.dataStore.edit { it[Keys.FAVORITES_JSON] = json.encodeToString(items) } }
    }

    suspend fun saveHistory(items: List<SavedTextItem>) {
        runCatching { context.dataStore.edit { it[Keys.HISTORY_JSON] = json.encodeToString(items.take(60)) } }
    }

    private fun decodeList(raw: String?): List<SavedTextItem> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<SavedTextItem>>(raw) }.getOrDefault(emptyList())
    }
}
