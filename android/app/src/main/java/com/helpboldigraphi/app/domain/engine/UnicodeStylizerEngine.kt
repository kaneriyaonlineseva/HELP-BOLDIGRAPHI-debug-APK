package com.helpboldigraphi.app.domain.engine

import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.RenderMode
import com.helpboldigraphi.app.domain.model.ScriptKind
import com.helpboldigraphi.app.domain.model.StyleCategory
import com.helpboldigraphi.app.domain.model.StylePreset

object UnicodeStylizerEngine {

    private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"

    private fun mapByOffset(
        text: String,
        upperStart: Int,
        lowerStart: Int,
        digitStart: Int? = null,
        exceptions: Map<Char, String> = emptyMap()
    ): String {
        val sb = StringBuilder()
        for (ch in text) {
            val ex = exceptions[ch]
            if (ex != null) {
                sb.append(ex)
                continue
            }
            val uIdx = UPPER.indexOf(ch)
            if (uIdx != -1) {
                sb.appendCodePoint(upperStart + uIdx)
                continue
            }
            val lIdx = LOWER.indexOf(ch)
            if (lIdx != -1) {
                sb.appendCodePoint(lowerStart + lIdx)
                continue
            }
            if (digitStart != null) {
                val dIdx = DIGITS.indexOf(ch)
                if (dIdx != -1) {
                    sb.appendCodePoint(digitStart + dIdx)
                    continue
                }
            }
            sb.append(ch)
        }
        return sb.toString()
    }

    private fun mapByTable(text: String, table: Map<Char, String>): String {
        val sb = StringBuilder()
        for (ch in text) {
            sb.append(table[ch] ?: ch.toString())
        }
        return sb.toString()
    }

    private fun combinePerChar(text: String, combiningChar: Char): String {
        val sb = StringBuilder()
        for (ch in text) {
            sb.append(ch)
            if (ch != ' ' && ch != '\n') {
                sb.append(combiningChar)
            }
        }
        return sb.toString()
    }

    fun toBoldSerif(text: String): String = mapByOffset(text, 0x1D400, 0x1D41A, 0x1D7CE)
    fun toBoldSans(text: String): String = mapByOffset(text, 0x1D5D4, 0x1D5EE, 0x1D7EC)
    fun toItalicSerif(text: String): String = mapByOffset(text, 0x1D434, 0x1D44E, exceptions = mapOf('h' to "\u210E"))
    fun toBoldItalicSerif(text: String): String = mapByOffset(text, 0x1D468, 0x1D482)
    fun toBoldItalicSans(text: String): String = mapByOffset(text, 0x1D63C, 0x1D656)
    fun toScript(text: String): String = mapByOffset(
        text,
        0x1D49C,
        0x1D4B6,
        exceptions = mapOf(
            'B' to "\u212C", 'E' to "\u2130", 'F' to "\u2131", 'H' to "\u210B",
            'I' to "\u2110", 'L' to "\u2112", 'M' to "\u2133", 'R' to "\u211B",
            'e' to "\u212F", 'g' to "\u210A", 'o' to "\u2134"
        )
    )
    fun toBoldScript(text: String): String = mapByOffset(text, 0x1D4D0, 0x1D4EA)
    fun toGothic(text: String): String = mapByOffset(
        text,
        0x1D504,
        0x1D51E,
        exceptions = mapOf('C' to "\u212D", 'H' to "\u210C", 'I' to "\u2111", 'R' to "\u211C", 'Z' to "\u2128")
    )
    fun toGothicBold(text: String): String = mapByOffset(text, 0x1D56C, 0x1D586)
    fun toDoubleStruck(text: String): String = mapByOffset(
        text,
        0x1D538,
        0x1D552,
        0x1D7D8,
        exceptions = mapOf(
            'C' to "\u2102", 'H' to "\u210D", 'N' to "\u2115",
            'P' to "\u2119", 'Q' to "\u211A", 'R' to "\u211D", 'Z' to "\u2124"
        )
    )
    fun toMonospace(text: String): String = mapByOffset(text, 0x1D670, 0x1D68A, 0x1D7F6)

    private val smallCapsMap = mapOf(
        'a' to "ᴀ", 'b' to "ʙ", 'c' to "ᴄ", 'd' to "ᴅ", 'e' to "ᴇ", 'f' to "ꜰ",
        'g' to "ɢ", 'h' to "ʜ", 'i' to "ɪ", 'j' to "ᴊ", 'k' to "ᴋ", 'l' to "ʟ",
        'm' to "ᴍ", 'n' to "ɴ", 'o' to "ᴏ", 'p' to "ᴘ", 'q' to "ǫ", 'r' to "ʀ",
        's' to "s", 't' to "ᴛ", 'u' to "ᴜ", 'v' to "ᴠ", 'w' to "ᴡ", 'x' to "x",
        'y' to "ʏ", 'z' to "ᴢ"
    )

    fun toSmallCaps(text: String): String = mapByTable(text, smallCapsMap)

    fun toCircled(text: String): String = mapByOffset(
        text,
        0x24B6,
        0x24D0,
        exceptions = mapOf(
            '0' to "\u24EA", '1' to "\u2460", '2' to "\u2461", '3' to "\u2462", '4' to "\u2463",
            '5' to "\u2464", '6' to "\u2465", '7' to "\u2466", '8' to "\u2467", '9' to "\u2468"
        )
    )

    fun toDarkBubble(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            val idx = UPPER.indexOf(ch.uppercaseChar())
            if (idx != -1) sb.appendCodePoint(0x1F150 + idx) else sb.append(ch)
        }
        return sb.toString()
    }

    fun toBlackBoxed(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            val idx = UPPER.indexOf(ch.uppercaseChar())
            if (idx != -1) sb.appendCodePoint(0x1F170 + idx) else sb.append(ch)
        }
        return sb.toString()
    }

    fun toFullwidth(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            when {
                ch == ' ' -> sb.append('\u3000')
                ch.code in 33..126 -> sb.append((ch.code + 0xFEE0).toChar())
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    private val flipMap = mapOf(
        'a' to 'ɐ', 'b' to 'q', 'c' to 'ɔ', 'd' to 'p', 'e' to 'ǝ', 'f' to 'ɟ',
        'g' to 'ƃ', 'h' to 'ɥ', 'i' to 'ı', 'j' to 'ɾ', 'k' to 'ʞ', 'l' to 'l',
        'm' to 'ɯ', 'n' to 'u', 'o' to 'o', 'p' to 'd', 'q' to 'b', 'r' to 'ɹ',
        's' to 's', 't' to 'ʇ', 'u' to 'n', 'v' to 'ʌ', 'w' to 'ʍ', 'x' to 'x',
        'y' to 'ʎ', 'z' to 'z',
        'A' to '∀', 'C' to 'Ɔ', 'E' to 'Ǝ', 'F' to 'Ⅎ', 'G' to '⅁', 'L' to '˥',
        'M' to 'W', 'P' to 'Ԁ', 'T' to '⊥', 'U' to '∩', 'V' to 'Λ', 'W' to 'M',
        'Y' to '⅄', '!' to '¡', '?' to '¿'
    )

    fun toUpsideDown(text: String): String {
        val sb = StringBuilder()
        for (ch in text.reversed()) {
            sb.append(flipMap[ch] ?: ch)
        }
        return sb.toString()
    }

    private val devanagariDigits = mapOf(
        '0' to "०", '1' to "१", '2' to "२", '3' to "३", '4' to "४",
        '5' to "५", '6' to "६", '7' to "७", '8' to "८", '9' to "९"
    )

    fun toDevanagariDigits(text: String): String = mapByTable(text, devanagariDigits)

    fun containsDevanagari(text: String): Boolean = text.any { it.code in 0x0900..0x097F }

    fun toStrikethrough(text: String): String = combinePerChar(text, '\u0336')
    fun toDoubleUnderline(text: String): String = combinePerChar(text, '\u0333')

    val allPresets: List<StylePreset> = DevanagariCatalog.hindiPresets + listOf(
        StylePreset(
            id = "eng-bold-serif",
            name = "Obsidian Bold Serif",
            subtitle = "Unicode mathematical bold serif",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            isVip = true,
            fontType = BundledFontType.SYNE_DISPLAY,
            transform = ::toBoldSerif
        ),
        StylePreset(
            id = "eng-bold-sans",
            name = "Vanguard Bold Sans",
            subtitle = "Clean social bio bold",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            fontType = BundledFontType.NOTO_SANS_DEVANAGARI,
            transform = ::toBoldSans
        ),
        StylePreset(
            id = "eng-bold-italic",
            name = "Editorial Bold Italic",
            subtitle = "Slanted luxury serif glyphs",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            transform = ::toBoldItalicSerif
        ),
        StylePreset(
            id = "eng-bold-italic-sans",
            name = "Kinetic Bold Italic",
            subtitle = "Dynamic forward-leaning sans bold",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            transform = ::toBoldItalicSans
        ),
        StylePreset(
            id = "eng-italic-serif",
            name = "Classic Italic Serif",
            subtitle = "Refined literary emphasis",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            transform = ::toItalicSerif
        ),
        StylePreset(
            id = "eng-double-struck",
            name = "Double-Struck Outline",
            subtitle = "Engraved architectural lettering",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            isVip = true,
            fontType = BundledFontType.SPACE_GROTESK,
            transform = ::toDoubleStruck
        ),
        StylePreset(
            id = "eng-small-caps",
            name = "Royal Small Caps",
            subtitle = "Balanced small-cap proportions",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.BOLD,
            renderMode = RenderMode.UNICODE,
            fontType = BundledFontType.SPACE_GROTESK,
            transform = ::toSmallCaps
        ),
        StylePreset(
            id = "eng-bold-script",
            name = "Royal Bold Cursive",
            subtitle = "Flowing calligraphy script",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.SCRIPT,
            renderMode = RenderMode.UNICODE,
            isVip = true,
            transform = ::toBoldScript
        ),
        StylePreset(
            id = "eng-fine-script",
            name = "Silk Signature Script",
            subtitle = "Delicate handwritten cursive",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.SCRIPT,
            renderMode = RenderMode.UNICODE,
            transform = ::toScript
        ),
        StylePreset(
            id = "eng-gothic-bold",
            name = "Gothic Bold Fraktur",
            subtitle = "Heavy blackletter display",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.GOTHIC,
            renderMode = RenderMode.UNICODE,
            isVip = true,
            fontType = BundledFontType.SYNE_DISPLAY,
            transform = ::toGothicBold
        ),
        StylePreset(
            id = "eng-gothic-classic",
            name = "Fraktur Classic",
            subtitle = "Traditional blackletter",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.GOTHIC,
            renderMode = RenderMode.UNICODE,
            transform = ::toGothic
        ),
        StylePreset(
            id = "eng-monospace",
            name = "Studio Monospace",
            subtitle = "Fixed-width technical glyphs",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.GOTHIC,
            renderMode = RenderMode.UNICODE,
            fontType = BundledFontType.SPACE_GROTESK,
            transform = ::toMonospace
        ),
        StylePreset(
            id = "eng-fullwidth",
            name = "Vapor Fullwidth",
            subtitle = "Wide aesthetic spacing",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.GOTHIC,
            renderMode = RenderMode.UNICODE,
            transform = ::toFullwidth
        ),
        StylePreset(
            id = "eng-upside-down",
            name = "Mirror Flip Inverted",
            subtitle = "Rotated 180° Unicode flip",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.GOTHIC,
            renderMode = RenderMode.UNICODE,
            transform = ::toUpsideDown
        ),
        StylePreset(
            id = "dec-dark-bubble",
            name = "Obsidian Bubble Seal",
            subtitle = "Dark filled circular glyphs",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.UNICODE,
            isVip = true,
            transform = ::toDarkBubble
        ),
        StylePreset(
            id = "dec-circled",
            name = "Crystal Circled",
            subtitle = "Hollow ringed lettering",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.UNICODE,
            transform = ::toCircled
        ),
        StylePreset(
            id = "dec-black-boxed",
            name = "Monolith Tile Blocks",
            subtitle = "Solid square tile glyphs",
            script = ScriptKind.ENGLISH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.UNICODE,
            transform = ::toBlackBoxed
        ),
        StylePreset(
            id = "dec-crown-vip",
            name = "Royal Crown Signature",
            subtitle = "Sovereign frame + bold serif",
            script = ScriptKind.BOTH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.ORNAMENT,
            isVip = true,
            fontType = BundledFontType.SYNE_DISPLAY,
            transform = { "♛ ${toBoldSerif(it)} ♛" }
        ),
        StylePreset(
            id = "dec-wings",
            name = "Imperial Wings Crest",
            subtitle = "Symmetrical creator frame",
            script = ScriptKind.BOTH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.ORNAMENT,
            transform = { "꧁༒ ${toBoldSans(it)} ༒꧂" }
        ),
        StylePreset(
            id = "dec-brackets",
            name = "Studio Corner Brackets",
            subtitle = "Editorial headline framing",
            script = ScriptKind.BOTH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.ORNAMENT,
            fontType = BundledFontType.SPACE_GROTESK,
            transform = { "『 ${toSmallCaps(it)} 』" }
        ),
        StylePreset(
            id = "dec-strike",
            name = "Redacted Strikethrough",
            subtitle = "Crossed-line combining glyph",
            script = ScriptKind.BOTH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.UNICODE,
            transform = ::toStrikethrough
        ),
        StylePreset(
            id = "dec-underline",
            name = "Double Underlined",
            subtitle = "Emphasized dual-rule glyph",
            script = ScriptKind.BOTH,
            category = StyleCategory.DECORATIVE,
            renderMode = RenderMode.UNICODE,
            transform = ::toDoubleUnderline
        )
    )
}
