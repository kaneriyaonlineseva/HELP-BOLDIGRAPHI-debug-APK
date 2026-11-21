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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import com.helpboldigraphi.app.domain.model.SavedTextItem
import com.helpboldigraphi.app.ui.theme.FontFamilyRegistry
import com.helpboldigraphi.app.ui.theme.LocalizedStrings
import java.text.DateFormat
import java.util.Date

@Composable
fun FavoritesScreen(
    favorites: List<SavedTextItem>,
    strings: LocalizedStrings,
    onCopy: (SavedTextItem) -> Unit,
    onShare: (SavedTextItem) -> Unit,
    onExportPng: (SavedTextItem) -> Unit,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    var showConfirmClear by remember { mutableStateOf(false) }

    val filtered = remember(favorites, search) {
        val q = search.trim().lowercase()
        if (q.isEmpty()) favorites else favorites.filter {
            it.styleName.lowercase().contains(q) || it.styledText.lowercase().contains(q)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(strings.favoritesTitle, style = MaterialTheme.typography.titleLarge)
                if (favorites.isNotEmpty()) {
                    TextButton(onClick = { showConfirmClear = true }) {
                        Text(strings.clearFavorites)
                    }
                }
            }
        }

        if (favorites.isNotEmpty()) {
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text(strings.searchPlaceholder) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        if (favorites.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = strings.favoritesEmpty,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        } else {
            items(filtered, key = { it.id }) { item ->
                SavedItemCard(
                    item = item,
                    strings = strings,
                    onCopy = { onCopy(item) },
                    onShare = { onShare(item) },
                    onExportPng = { onExportPng(item) },
                    onUseInStudio = null,
                    onRemove = { onRemove(item.id) }
                )
            }
        }
    }

    if (showConfirmClear) {
        AlertDialog(
            onDismissRequest = { showConfirmClear = false },
            title = { Text(strings.confirmClearTitle) },
            confirmButton = {
                TextButton(onClick = {
                    onClearAll()
                    showConfirmClear = false
                }) {
                    Text(strings.confirmAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmClear = false }) {
                    Text(strings.cancelAction)
                }
            }
        )
    }
}

@Composable
fun HistoryScreen(
    history: List<SavedTextItem>,
    strings: LocalizedStrings,
    onCopy: (SavedTextItem) -> Unit,
    onShare: (SavedTextItem) -> Unit,
    onExportPng: (SavedTextItem) -> Unit,
    onUseInStudio: (SavedTextItem) -> Unit,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmClear by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(strings.historyTitle, style = MaterialTheme.typography.titleLarge)
                if (history.isNotEmpty()) {
                    TextButton(onClick = { showConfirmClear = true }) {
                        Text(strings.clearHistory)
                    }
                }
            }
        }

        if (history.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = strings.historyEmpty,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        } else {
            items(history, key = { it.id }) { item ->
                SavedItemCard(
                    item = item,
                    strings = strings,
                    onCopy = { onCopy(item) },
                    onShare = { onShare(item) },
                    onExportPng = { onExportPng(item) },
                    onUseInStudio = { onUseInStudio(item) },
                    onRemove = { onRemove(item.id) }
                )
            }
        }
    }

    if (showConfirmClear) {
        AlertDialog(
            onDismissRequest = { showConfirmClear = false },
            title = { Text(strings.confirmClearTitle) },
            confirmButton = {
                TextButton(onClick = {
                    onClearAll()
                    showConfirmClear = false
                }) {
                    Text(strings.confirmAction)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmClear = false }) {
                    Text(strings.cancelAction)
                }
            }
        )
    }
}

@Composable
private fun SavedItemCard(
    item: SavedTextItem,
    strings: LocalizedStrings,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onExportPng: () -> Unit,
    onUseInStudio: (() -> Unit)?,
    onRemove: () -> Unit
) {
    val formattedTime = remember(item.createdAt) {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(item.createdAt))
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.styleName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${item.actionType.uppercase()} · $formattedTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = item.styledText,
                    fontFamily = FontFamilyRegistry.resolveByName(item.fontTypeName),
                    fontSize = 18.sp,
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCopy,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(strings.copyAction)
                }
                OutlinedButton(
                    onClick = onShare,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = strings.shareAction, modifier = Modifier.size(16.dp))
                }
                OutlinedButton(
                    onClick = onExportPng,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = strings.pngAction,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (onUseInStudio != null) {
                    OutlinedButton(
                        onClick = onUseInStudio,
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = strings.useInStudio, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
