package com.helpboldigraphi.app.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.ui.theme.LocalizedStrings

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    appLanguage: AppLanguage,
    favoritesCount: Int,
    historyCount: Int,
    strings: LocalizedStrings,
    onToggleTheme: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onClearFavorites: () -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pendingClearAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(strings.settingsTitle, style = MaterialTheme.typography.titleLarge)
        }

        // Language Selection (English / हिन्दी)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(strings.languageSection, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppLanguage.entries.forEach { lang ->
                            FilterChip(
                                selected = appLanguage == lang,
                                onClick = { onSelectLanguage(lang) },
                                label = { Text(lang.displayName) }
                            )
                        }
                    }
                }
            }
        }

        // Dark / Light Theme
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(strings.themeSection, style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (isDarkMode) strings.darkModeLabel else strings.lightModeLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = isDarkMode, onCheckedChange = { onToggleTheme() })
                }
            }
        }

        // Offline Storage Management
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(strings.storageSection, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        strings.offlineNotice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { pendingClearAction = onClearFavorites },
                            enabled = favoritesCount > 0,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text("${strings.clearFavorites} ($favoritesCount)")
                        }
                        OutlinedButton(
                            onClick = { pendingClearAction = onClearHistory },
                            enabled = historyCount > 0,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text("${strings.clearHistory} ($historyCount)")
                        }
                    }
                }
            }
        }

        // Branding & Font Licensing Summary
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(strings.appName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        strings.tagline,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Bundled OFL 1.1 Devanagari & Latin Fonts: Mukta, Hind, Noto Sans Devanagari, Tiro Devanagari Hindi, Rozha One, Syne, Space Grotesk.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    pendingClearAction?.let { clearFn ->
        AlertDialog(
            onDismissRequest = { pendingClearAction = null },
            title = { Text(strings.confirmClearTitle) },
            confirmButton = {
                TextButton(onClick = {
                    clearFn()
                    pendingClearAction = null
                }) {
                    Text(strings.confirmAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingClearAction = null }) {
                    Text(strings.cancelAction)
                }
            }
        )
    }
}
