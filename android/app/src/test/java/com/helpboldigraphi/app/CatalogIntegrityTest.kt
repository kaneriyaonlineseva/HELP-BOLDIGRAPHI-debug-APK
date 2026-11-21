package com.helpboldigraphi.app

import com.helpboldigraphi.app.domain.engine.DevanagariCatalog
import com.helpboldigraphi.app.domain.engine.SocialTemplatesCatalog
import com.helpboldigraphi.app.domain.engine.UnicodeStylizerEngine
import com.helpboldigraphi.app.domain.model.BundledFontType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogIntegrityTest {

    @Test
    fun allStylePresetIdsAreUnique() {
        val ids = UnicodeStylizerEngine.allPresets.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun devanagariCatalogIncludesCoreOflFonts() {
        val fontTypes = DevanagariCatalog.hindiPresets.map { it.fontType }.toSet()
        assertTrue(fontTypes.contains(BundledFontType.MUKTA))
        assertTrue(fontTypes.contains(BundledFontType.HIND))
        assertTrue(fontTypes.contains(BundledFontType.NOTO_SANS_DEVANAGARI))
        assertTrue(fontTypes.contains(BundledFontType.TIRO_DEVANAGARI))
        assertTrue(fontTypes.contains(BundledFontType.ROZHA_ONE))
    }

    @Test
    fun socialTemplatesHaveUniqueIdsAndNonBlankContent() {
        val ids = SocialTemplatesCatalog.templates.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        SocialTemplatesCatalog.templates.forEach { tpl ->
            assertTrue(tpl.titleEn.isNotBlank())
            assertTrue(tpl.titleHi.isNotBlank())
            assertTrue(tpl.body.isNotBlank())
        }
    }
}
