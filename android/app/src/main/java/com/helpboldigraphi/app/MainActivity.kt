package com.helpboldigraphi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.StylePreset
import com.helpboldigraphi.app.platform.AndroidShareHelper
import com.helpboldigraphi.app.platform.PlatformResult
import com.helpboldigraphi.app.ui.components.PngExportSheet
import com.helpboldigraphi.app.ui.components.ShareSheet
import com.helpboldigraphi.app.ui.screens.FavoritesScreen
import com.helpboldigraphi.app.ui.screens.HistoryScreen
import com.helpboldigraphi.app.ui.screens.SettingsScreen
import com.helpboldigraphi.app.ui.screens.StudioScreen
import com.helpboldigraphi.app.ui.screens.TemplatesScreen
import com.helpboldigraphi.app.ui.theme.AppStrings
import com.helpboldigraphi.app.ui.theme.HelpBoldigraphiTheme
import com.helpboldigraphi.app.ui.viewmodel.StudioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class MainTab {
    STUDIO,
    TEMPLATES,
    FAVORITES,
    HISTORY,
    SETTINGS
}

private data class PngModalTarget(
    val styleId: String,
    val styleName: String,
    val text: String,
    val fontType: BundledFontType,
    val useBold: Boolean
)

private data class ShareModalTarget(
    val styleId: String,
    val styleName: String,
    val text: String,
    val fontType: BundledFontType
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: StudioViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            val strings = remember(state.appLanguage) { AppStrings.forLanguage(state.appLanguage) }

            var activeTab by remember { mutableStateOf(MainTab.STUDIO) }
            var copiedPresetId by remember { mutableStateOf<String?>(null) }
            var shareTarget by remember { mutableStateOf<ShareModalTarget?>(null) }
            var pngTarget by remember { mutableStateOf<PngModalTarget?>(null) }

            fun notifyUser(message: String) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message)
                }
            }

            HelpBoldigraphiTheme(darkTheme = state.isDarkMode) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text(strings.appName, style = MaterialTheme.typography.titleLarge) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            actions = {
                                IconButton(
                                    onClick = {
                                        val next = if (state.appLanguage == AppLanguage.ENGLISH) {
                                            AppLanguage.HINDI
                                        } else {
                                            AppLanguage.ENGLISH
                                        }
                                        viewModel.setAppLanguage(next)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Translate,
                                        contentDescription = "Switch App Language"
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleTheme() }) {
                                    Icon(
                                        imageVector = if (state.isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                        contentDescription = "Toggle Light or Dark mode"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            NavigationBarItem(
                                selected = activeTab == MainTab.STUDIO,
                                onClick = { activeTab = MainTab.STUDIO },
                                icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null) },
                                label = { Text(strings.navStudio) }
                            )
                            NavigationBarItem(
                                selected = activeTab == MainTab.TEMPLATES,
                                onClick = { activeTab = MainTab.TEMPLATES },
                                icon = { Icon(Icons.Filled.Dashboard, contentDescription = null) },
                                label = { Text(strings.navTemplates) }
                            )
                            NavigationBarItem(
                                selected = activeTab == MainTab.FAVORITES,
                                onClick = { activeTab = MainTab.FAVORITES },
                                icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                                label = { Text(strings.navFavorites) }
                            )
                            NavigationBarItem(
                                selected = activeTab == MainTab.HISTORY,
                                onClick = { activeTab = MainTab.HISTORY },
                                icon = { Icon(Icons.Filled.History, contentDescription = null) },
                                label = { Text(strings.navHistory) }
                            )
                            NavigationBarItem(
                                selected = activeTab == MainTab.SETTINGS,
                                onClick = { activeTab = MainTab.SETTINGS },
                                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                                label = { Text(strings.navSettings) }
                            )
                        }
                    }
                ) { innerPadding ->
                    when (activeTab) {
                        MainTab.STUDIO -> StudioScreen(
                            state = state,
                            strings = strings,
                            copiedPresetId = copiedPresetId,
                            onInputChange = viewModel::updateInputText,
                            onSearchChange = viewModel::updateSearchQuery,
                            onScriptChange = viewModel::updateScriptFilter,
                            onCategoryChange = viewModel::updateCategory,
                            onFontSizeChange = viewModel::updateFontSize,
                            onResetFilters = viewModel::resetStudioFilters,
                            onCopy = { preset: StylePreset, styled: String ->
                                when (val res = AndroidShareHelper.copyToClipboard(context, preset.name, styled)) {
                                    PlatformResult.Success -> {
                                        viewModel.recordHistory(preset.id, preset.name, styled, "copied", preset.fontType)
                                        copiedPresetId = preset.id
                                        notifyUser("${strings.copiedToast}: ${preset.name}")
                                        scope.launch {
                                            delay(1600)
                                            if (copiedPresetId == preset.id) copiedPresetId = null
                                        }
                                    }
                                    is PlatformResult.Error -> notifyUser(res.message)
                                    is PlatformResult.Fallback -> notifyUser(res.message)
                                }
                            },
                            onShare = { preset, styled ->
                                shareTarget = ShareModalTarget(preset.id, preset.name, styled, preset.fontType)
                            },
                            onExportPng = { preset, styled ->
                                pngTarget = PngModalTarget(
                                    styleId = preset.id,
                                    styleName = preset.name,
                                    text = styled,
                                    fontType = preset.fontType,
                                    useBold = preset.useBoldWeight
                                )
                            },
                            onToggleFavorite = viewModel::toggleFavorite,
                            modifier = Modifier.padding(innerPadding)
                        )

                        MainTab.TEMPLATES -> TemplatesScreen(
                            templates = state.filteredTemplates,
                            selectedCategory = state.selectedTemplateCategory,
                            language = state.appLanguage,
                            strings = strings,
                            onCategoryChange = viewModel::updateTemplateCategory,
                            onCopy = { tpl ->
                                val title = if (state.appLanguage == AppLanguage.HINDI) tpl.titleHi else tpl.titleEn
                                when (val res = AndroidShareHelper.copyToClipboard(context, title, tpl.body)) {
                                    PlatformResult.Success -> {
                                        viewModel.recordHistory(tpl.id, title, tpl.body, "copied", tpl.fontType)
                                        notifyUser("${strings.copiedToast}: $title")
                                    }
                                    is PlatformResult.Error -> notifyUser(res.message)
                                    is PlatformResult.Fallback -> notifyUser(res.message)
                                }
                            },
                            onShare = { tpl ->
                                val title = if (state.appLanguage == AppLanguage.HINDI) tpl.titleHi else tpl.titleEn
                                shareTarget = ShareModalTarget(tpl.id, title, tpl.body, tpl.fontType)
                            },
                            onExportPng = { tpl ->
                                val title = if (state.appLanguage == AppLanguage.HINDI) tpl.titleHi else tpl.titleEn
                                pngTarget = PngModalTarget(
                                    styleId = tpl.id,
                                    styleName = title,
                                    text = tpl.body,
                                    fontType = tpl.fontType,
                                    useBold = false
                                )
                            },
                            onUseInStudio = { tpl ->
                                viewModel.updateInputText(tpl.body)
                                activeTab = MainTab.STUDIO
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        MainTab.FAVORITES -> FavoritesScreen(
                            favorites = state.favorites,
                            strings = strings,
                            onCopy = { item ->
                                when (val res = AndroidShareHelper.copyToClipboard(context, item.styleName, item.styledText)) {
                                    PlatformResult.Success -> notifyUser(strings.copiedToast)
                                    is PlatformResult.Error -> notifyUser(res.message)
                                    is PlatformResult.Fallback -> notifyUser(res.message)
                                }
                            },
                            onShare = { item ->
                                val font = BundledFontType.entries.find { it.name == item.fontTypeName }
                                    ?: BundledFontType.DEFAULT_SANS
                                shareTarget = ShareModalTarget(item.styleId, item.styleName, item.styledText, font)
                            },
                            onExportPng = { item ->
                                val font = BundledFontType.entries.find { it.name == item.fontTypeName }
                                    ?: BundledFontType.DEFAULT_SANS
                                pngTarget = PngModalTarget(item.styleId, item.styleName, item.styledText, font, false)
                            },
                            onRemove = viewModel::removeFavorite,
                            onClearAll = viewModel::clearFavorites,
                            modifier = Modifier.padding(innerPadding)
                        )

                        MainTab.HISTORY -> HistoryScreen(
                            history = state.history,
                            strings = strings,
                            onCopy = { item ->
                                when (val res = AndroidShareHelper.copyToClipboard(context, item.styleName, item.styledText)) {
                                    PlatformResult.Success -> notifyUser(strings.copiedToast)
                                    is PlatformResult.Error -> notifyUser(res.message)
                                    is PlatformResult.Fallback -> notifyUser(res.message)
                                }
                            },
                            onShare = { item ->
                                val font = BundledFontType.entries.find { it.name == item.fontTypeName }
                                    ?: BundledFontType.DEFAULT_SANS
                                shareTarget = ShareModalTarget(item.styleId, item.styleName, item.styledText, font)
                            },
                            onExportPng = { item ->
                                val font = BundledFontType.entries.find { it.name == item.fontTypeName }
                                    ?: BundledFontType.DEFAULT_SANS
                                pngTarget = PngModalTarget(item.styleId, item.styleName, item.styledText, font, false)
                            },
                            onUseInStudio = { item ->
                                viewModel.updateInputText(item.sourceText)
                                activeTab = MainTab.STUDIO
                            },
                            onRemove = viewModel::removeHistoryItem,
                            onClearAll = viewModel::clearHistory,
                            modifier = Modifier.padding(innerPadding)
                        )

                        MainTab.SETTINGS -> SettingsScreen(
                            isDarkMode = state.isDarkMode,
                            appLanguage = state.appLanguage,
                            favoritesCount = state.favorites.size,
                            historyCount = state.history.size,
                            strings = strings,
                            onToggleTheme = viewModel::toggleTheme,
                            onSelectLanguage = viewModel::setAppLanguage,
                            onClearFavorites = viewModel::clearFavorites,
                            onClearHistory = viewModel::clearHistory,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }

                shareTarget?.let { target ->
                    ShareSheet(
                        text = target.text,
                        styleName = target.styleName,
                        strings = strings,
                        onDismiss = { shareTarget = null },
                        onCopy = {
                            when (val res = AndroidShareHelper.copyToClipboard(context, target.styleName, target.text)) {
                                PlatformResult.Success -> {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "copied", target.fontType)
                                    notifyUser(strings.copiedToast)
                                }
                                is PlatformResult.Error -> notifyUser(res.message)
                                is PlatformResult.Fallback -> notifyUser(res.message)
                            }
                            shareTarget = null
                        },
                        onSystemShare = {
                            when (val res = AndroidShareHelper.shareTextSystem(context, target.text)) {
                                PlatformResult.Success -> {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "shared", target.fontType)
                                }
                                is PlatformResult.Error -> notifyUser(res.message)
                                is PlatformResult.Fallback -> notifyUser(res.message)
                            }
                            shareTarget = null
                        },
                        onPlatformShare = { platform ->
                            when (val res = AndroidShareHelper.shareTextToPlatform(context, target.text, platform)) {
                                PlatformResult.Success -> {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "shared", target.fontType)
                                }
                                is PlatformResult.Fallback -> {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "shared", target.fontType)
                                    notifyUser(res.message)
                                }
                                is PlatformResult.Error -> notifyUser(res.message)
                            }
                            shareTarget = null
                        }
                    )
                }

                pngTarget?.let { target ->
                    PngExportSheet(
                        text = target.text,
                        styleName = target.styleName,
                        fontType = target.fontType,
                        useBold = target.useBold,
                        strings = strings,
                        onDismiss = { pngTarget = null },
                        onSaveToGallery = { bitmap ->
                            AndroidShareHelper.savePngToGallery(context, bitmap)
                                .onSuccess {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "exported", target.fontType)
                                    notifyUser(strings.savedPngToast)
                                }
                                .onFailure { err ->
                                    notifyUser(err.localizedMessage ?: "Failed to save PNG")
                                }
                            pngTarget = null
                        },
                        onShareBitmap = { bitmap ->
                            AndroidShareHelper.exportAndSharePng(context, bitmap)
                                .onSuccess {
                                    viewModel.recordHistory(target.styleId, target.styleName, target.text, "exported", target.fontType)
                                }
                                .onFailure { err ->
                                    notifyUser(err.localizedMessage ?: "Failed to share PNG")
                                }
                            pngTarget = null
                        }
                    )
                }
            }
        }
    }
}
