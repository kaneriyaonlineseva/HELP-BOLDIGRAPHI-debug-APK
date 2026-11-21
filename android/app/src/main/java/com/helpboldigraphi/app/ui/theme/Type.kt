package com.helpboldigraphi.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.helpboldigraphi.app.R
import com.helpboldigraphi.app.domain.model.BundledFontType

object FontFamilyRegistry {
    val SyneDisplay = FontFamily(
        Font(R.font.syne_bold, FontWeight.Bold),
        Font(R.font.syne_bold, FontWeight.ExtraBold)
    )

    val SpaceGrotesk = FontFamily(
        Font(R.font.space_grotesk_semibold, FontWeight.SemiBold)
    )

    val Mukta = FontFamily(
        Font(R.font.mukta_regular, FontWeight.Normal),
        Font(R.font.mukta_bold, FontWeight.Bold)
    )

    val Hind = FontFamily(
        Font(R.font.hind_regular, FontWeight.Normal),
        Font(R.font.hind_bold, FontWeight.Bold)
    )

    val NotoSansDevanagari = FontFamily(
        Font(R.font.noto_sans_devanagari_regular, FontWeight.Normal),
        Font(R.font.noto_sans_devanagari_regular, FontWeight.SemiBold)
    )

    val TiroDevanagari = FontFamily(
        Font(R.font.tiro_devanagari_hindi_regular, FontWeight.Normal)
    )

    val RozhaOne = FontFamily(
        Font(R.font.rozha_one_regular, FontWeight.Normal)
    )

    fun resolve(fontType: BundledFontType): FontFamily = when (fontType) {
        BundledFontType.ROZHA_ONE -> RozhaOne
        BundledFontType.MUKTA -> Mukta
        BundledFontType.HIND -> Hind
        BundledFontType.NOTO_SANS_DEVANAGARI -> NotoSansDevanagari
        BundledFontType.TIRO_DEVANAGARI -> TiroDevanagari
        BundledFontType.SYNE_DISPLAY -> SyneDisplay
        BundledFontType.SPACE_GROTESK -> SpaceGrotesk
        BundledFontType.DEFAULT_SANS -> NotoSansDevanagari
    }

    fun resolveByName(name: String): FontFamily {
        val type = BundledFontType.entries.find { it.name == name } ?: BundledFontType.DEFAULT_SANS
        return resolve(type)
    }
}

val BoldigraphiTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamilyRegistry.SyneDisplay,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamilyRegistry.SyneDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamilyRegistry.NotoSansDevanagari,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamilyRegistry.NotoSansDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamilyRegistry.NotoSansDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamilyRegistry.SpaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
)
