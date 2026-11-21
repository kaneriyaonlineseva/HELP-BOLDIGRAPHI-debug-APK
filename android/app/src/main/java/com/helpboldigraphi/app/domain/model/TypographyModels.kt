package com.helpboldigraphi.app.domain.model

import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import com.helpboldigraphi.app.R
import kotlinx.serialization.Serializable

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिन्दी")
}

enum class ScriptKind {
    ENGLISH,
    HINDI,
    BOTH
}

enum class ScriptFilter {
    ALL,
    ENGLISH,
    HINDI
}

enum class StyleCategory {
    ALL,
    DEVANAGARI,
    BOLD,
    SCRIPT,
    GOTHIC,
    DECORATIVE
}

enum class RenderMode {
    UNICODE,
    FONT,
    ORNAMENT
}

enum class BundledFontType(
    @FontRes val regularRes: Int,
    @FontRes val boldRes: Int = regularRes
) {
    DEFAULT_SANS(R.font.noto_sans_devanagari_regular, R.font.noto_sans_devanagari_regular),
    ROZHA_ONE(R.font.rozha_one_regular, R.font.rozha_one_regular),
    MUKTA(R.font.mukta_regular, R.font.mukta_bold),
    HIND(R.font.hind_regular, R.font.hind_bold),
    NOTO_SANS_DEVANAGARI(R.font.noto_sans_devanagari_regular, R.font.noto_sans_devanagari_regular),
    TIRO_DEVANAGARI(R.font.tiro_devanagari_hindi_regular, R.font.tiro_devanagari_hindi_regular),
    SYNE_DISPLAY(R.font.syne_bold, R.font.syne_bold),
    SPACE_GROTESK(R.font.space_grotesk_semibold, R.font.space_grotesk_semibold)
}

enum class PngThemePreset(
    val id: String,
    val displayName: String,
    @ColorInt val bgStartArgb: Int,
    @ColorInt val bgEndArgb: Int,
    @ColorInt val borderArgb: Int,
    @ColorInt val accentArgb: Int,
    @ColorInt val textArgb: Int,
    @ColorInt val mutedArgb: Int
) {
    OBSIDIAN_VIOLET(
        id = "obsidian-violet",
        displayName = "Obsidian Violet",
        bgStartArgb = 0xFF141424.toInt(),
        bgEndArgb = 0xFF0B0B12.toInt(),
        borderArgb = 0xFF3B2F63.toInt(),
        accentArgb = 0xFFA078FF.toInt(),
        textArgb = 0xFFF8FAFC.toInt(),
        mutedArgb = 0xFF94A3B8.toInt()
    ),
    ROYAL_GOLD(
        id = "royal-gold",
        displayName = "Royal Gold",
        bgStartArgb = 0xFF1B1610.toInt(),
        bgEndArgb = 0xFF0D0B08.toInt(),
        borderArgb = 0xFF785113.toInt(),
        accentArgb = 0xFFFBBF24.toInt(),
        textArgb = 0xFFFFFBEB.toInt(),
        mutedArgb = 0xFFD6B98C.toInt()
    ),
    MIDNIGHT_INK(
        id = "midnight-ink",
        displayName = "Midnight Slate",
        bgStartArgb = 0xFF121826.toInt(),
        bgEndArgb = 0xFF080B12.toInt(),
        borderArgb = 0xFF28354D.toInt(),
        accentArgb = 0xFF7C3AED.toInt(),
        textArgb = 0xFFF1F5F9.toInt(),
        mutedArgb = 0xFF94A3B8.toInt()
    ),
    IVORY_LUXE(
        id = "ivory-luxe",
        displayName = "Ivory Editorial",
        bgStartArgb = 0xFFFFFFFF.toInt(),
        bgEndArgb = 0xFFEFEBF7.toInt(),
        borderArgb = 0xFFD2C8E8.toInt(),
        accentArgb = 0xFF6D28D9.toInt(),
        textArgb = 0xFF13131A.toInt(),
        mutedArgb = 0xFF5B5570.toInt()
    )
}

enum class PngAspectRatio(val label: String, val width: Int, val height: Int) {
    SQUARE("1:1", 1080, 1080),
    PORTRAIT("4:5", 1080, 1350),
    LANDSCAPE("16:9", 1280, 720)
}

enum class TemplateCategory {
    ALL,
    INSTAGRAM_BIO,
    WHATSAPP_STATUS,
    SHAYARI_QUOTES,
    FESTIVE_WISHES,
    CREATOR_HOOKS
}

data class SocialTemplate(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val category: TemplateCategory,
    val script: ScriptKind,
    val fontType: BundledFontType,
    val body: String
)

data class StylePreset(
    val id: String,
    val name: String,
    val subtitle: String,
    val script: ScriptKind,
    val category: StyleCategory,
    val renderMode: RenderMode,
    val isVip: Boolean = false,
    val fontType: BundledFontType = BundledFontType.DEFAULT_SANS,
    val useBoldWeight: Boolean = false,
    val transform: (String) -> String
)

@Serializable
data class SavedTextItem(
    val id: String,
    val sourceText: String,
    val styledText: String,
    val styleId: String,
    val styleName: String,
    val createdAt: Long,
    val actionType: String = "copied",
    val fontTypeName: String = BundledFontType.DEFAULT_SANS.name
)
