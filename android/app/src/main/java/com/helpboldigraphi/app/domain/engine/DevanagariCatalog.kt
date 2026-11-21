package com.helpboldigraphi.app.domain.engine

import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.RenderMode
import com.helpboldigraphi.app.domain.model.ScriptKind
import com.helpboldigraphi.app.domain.model.StyleCategory
import com.helpboldigraphi.app.domain.model.StylePreset

object DevanagariCatalog {
    val hindiPresets: List<StylePreset> = listOf(
        StylePreset(
            id = "dev-rozha-royal",
            name = "Rozha Royal Display",
            subtitle = "High-contrast Devanagari editorial serif",
            script = ScriptKind.HINDI,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.FONT,
            isVip = true,
            fontType = BundledFontType.ROZHA_ONE,
            transform = { "✦ ${UnicodeStylizerEngine.toDevanagariDigits(it)} ✦" }
        ),
        StylePreset(
            id = "dev-mukta-modern",
            name = "Mukta Sans Clean",
            subtitle = "Balanced matra & conjunct legibility",
            script = ScriptKind.HINDI,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.FONT,
            fontType = BundledFontType.MUKTA,
            useBoldWeight = true,
            transform = { it }
        ),
        StylePreset(
            id = "dev-hind-bold",
            name = "Hind Bold Studio",
            subtitle = "Crisp social quote Devanagari",
            script = ScriptKind.HINDI,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.FONT,
            fontType = BundledFontType.HIND,
            useBoldWeight = true,
            transform = { it }
        ),
        StylePreset(
            id = "dev-noto-clean",
            name = "Noto Sans Devanagari",
            subtitle = "Universal sharp UI & status Devanagari",
            script = ScriptKind.HINDI,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.FONT,
            fontType = BundledFontType.NOTO_SANS_DEVANAGARI,
            transform = { it }
        ),
        StylePreset(
            id = "dev-tiro-classic",
            name = "Tiro Classic Literature",
            subtitle = "Traditional poetry & literature Devanagari",
            script = ScriptKind.HINDI,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.FONT,
            isVip = true,
            fontType = BundledFontType.TIRO_DEVANAGARI,
            transform = { "❝ $it ❞" }
        ),
        StylePreset(
            id = "dev-ornament-mandala",
            name = "Dev-Royal Mandala Frame",
            subtitle = "Copy-paste Hindi & English ornamental seal",
            script = ScriptKind.BOTH,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.ORNAMENT,
            isVip = true,
            fontType = BundledFontType.ROZHA_ONE,
            transform = { "꧁❖ ${UnicodeStylizerEngine.toDevanagariDigits(it)} ❖꧂" }
        ),
        StylePreset(
            id = "dev-ornament-lotus",
            name = "Shubh Floral Crown",
            subtitle = "Copy-paste festive & devotional frame",
            script = ScriptKind.BOTH,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.ORNAMENT,
            fontType = BundledFontType.MUKTA,
            useBoldWeight = true,
            transform = { "❀⋆｡ $it ｡⋆❀" }
        ),
        StylePreset(
            id = "dev-ornament-shloka",
            name = "Shloka Bracket Seal",
            subtitle = "Editorial Sanskrit & Hindi quote border",
            script = ScriptKind.BOTH,
            category = StyleCategory.DEVANAGARI,
            renderMode = RenderMode.ORNAMENT,
            fontType = BundledFontType.TIRO_DEVANAGARI,
            transform = { "॥ ${UnicodeStylizerEngine.toDevanagariDigits(it)} ॥" }
        )
    )
}
