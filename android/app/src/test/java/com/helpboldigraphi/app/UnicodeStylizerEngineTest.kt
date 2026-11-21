package com.helpboldigraphi.app

import com.helpboldigraphi.app.domain.engine.UnicodeStylizerEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UnicodeStylizerEngineTest {

    @Test
    fun boldSerifTransformsAsciiLettersAndPreservesSpaces() {
        val result = UnicodeStylizerEngine.toBoldSerif("Help 2026")
        assertEquals("𝐇𝐞𝐥𝐩 𝟐𝟎𝟐𝟔", result)
    }

    @Test
    fun gothicTransformsAndPreservesDevanagariUntouched() {
        val input = "Style भारत"
        val result = UnicodeStylizerEngine.toGothicBold(input)
        assertTrue(result.endsWith("भारत"))
        assertFalse(result.startsWith("Style"))
    }

    @Test
    fun devanagariDigitsConvertCorrectly() {
        assertEquals("२०२६", UnicodeStylizerEngine.toDevanagariDigits("2026"))
    }

    @Test
    fun containsDevanagariDetectsHindiScript() {
        assertTrue(UnicodeStylizerEngine.containsDevanagari("नमस्ते"))
        assertFalse(UnicodeStylizerEngine.containsDevanagari("Hello World"))
    }

    @Test
    fun upsideDownReversesAndFlipsLetters() {
        val flipped = UnicodeStylizerEngine.toUpsideDown("hi")
        assertEquals("ıɥ", flipped)
    }

    @Test
    fun allPresetsProduceNonEmptyOutputForValidInput() {
        val sample = "Boldigraphi भारत 123"
        UnicodeStylizerEngine.allPresets.forEach { preset ->
            val output = preset.transform(sample)
            assertTrue("Preset ${preset.id} returned blank output", output.isNotBlank())
        }
    }
}
