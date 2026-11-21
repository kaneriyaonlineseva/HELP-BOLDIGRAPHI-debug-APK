package com.helpboldigraphi.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpboldigraphi.app.domain.model.ScriptFilter
import com.helpboldigraphi.app.domain.model.StyleCategory
import com.helpboldigraphi.app.domain.model.StylePreset
import com.helpboldigraphi.app.ui.components.StyleCard
import com.helpboldigraphi.app.ui.theme.AppStrings
import com.helpboldigraphi.app.ui.theme.LocalizedStrings
import com.helpboldigraphi.app.ui.viewmodel.StudioUiState

@Composable
fun StudioScreen(
    state: StudioUiState,
    strings: LocalizedStrings,
    copiedPresetId: String?,
    onInputChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onScriptChange: (ScriptFilter) -> Unit,
    onCategoryChange: (StyleCategory) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onResetFilters: () -> Unit,
    onCopy: (StylePreset, String) -> Unit,
    onShare: (StylePreset, String) -> Unit,
    onExportPng: (StylePreset, String) -> Unit,
    onToggleFavorite: (StylePreset, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val effectiveText = state.inputText.ifBlank { "HELP BOLDIGRAPHI\nआपका स्वागत है" }
    val samples = listOf(
        "Bilingual" to "HELP BOLDIGRAPHI\nआपका स्वागत है",
        "English" to "Your Text, Your Style!",
        "हिन्दी" to "शब्दों की अपनी कलाकारी है",
        "शुभकामना" to "सुप्रभात · २०२६"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = strings.tagline,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strings.subtitleBilingual,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.inputText,
                        onValueChange = onInputChange,
                        placeholder = { Text(strings.inputPlaceholder) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        trailingIcon = {
                            if (state.inputText.isNotEmpty()) {
                                IconButton(onClick = { onInputChange("") }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        samples.forEach { (label, sampleText) ->
                            SuggestionChip(
                                onClick = { onInputChange(sampleText) },
                                label = { Text(label) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ScriptFilter.entries.forEach { filter ->
                            FilterChip(
                                selected = state.scriptFilter == filter,
                                onClick = { onScriptChange(filter) },
                                label = { Text(AppStrings.scriptFilterLabel(filter, state.appLanguage)) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${strings.previewSizeLabel}: ${state.previewFontSizeSp.toInt()}sp",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            value = state.previewFontSizeSp,
                            onValueChange = onFontSizeChange,
                            valueRange = 16f..34f,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(strings.searchPlaceholder) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StyleCategory.entries.forEach { category ->
                    FilterChip(
                        selected = state.selectedCategory == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(AppStrings.categoryLabel(category, state.appLanguage)) }
                    )
                }
            }
        }

        if (state.filteredPresets.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = strings.emptyStyles,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onResetFilters, shape = RoundedCornerShape(999.dp)) {
                            Text(strings.resetFilters)
                        }
                    }
                }
            }
        } else {
            items(state.filteredPresets, key = { it.id }) { preset ->
                val styled = preset.transform(effectiveText)
                val isFav = state.favorites.any { it.styleId == preset.id && it.styledText == styled }
                StyleCard(
                    preset = preset,
                    renderedText = styled,
                    previewFontSizeSp = state.previewFontSizeSp,
                    language = state.appLanguage,
                    strings = strings,
                    isCopied = copiedPresetId == preset.id,
                    isFavorited = isFav,
                    onCopy = { onCopy(preset, styled) },
                    onShare = { onShare(preset, styled) },
                    onExportPng = { onExportPng(preset, styled) },
                    onToggleFavorite = { onToggleFavorite(preset, styled) }
                )
            }
        }
    }
}
