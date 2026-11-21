package com.helpboldigraphi.app.ui.theme

import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.RenderMode
import com.helpboldigraphi.app.domain.model.ScriptFilter
import com.helpboldigraphi.app.domain.model.StyleCategory
import com.helpboldigraphi.app.domain.model.TemplateCategory

data class LocalizedStrings(
    val appName: String,
    val tagline: String,
    val subtitleBilingual: String,
    val navStudio: String,
    val navTemplates: String,
    val navFavorites: String,
    val navHistory: String,
    val navSettings: String,
    val inputPlaceholder: String,
    val searchPlaceholder: String,
    val previewSizeLabel: String,
    val copyAction: String,
    val copiedAction: String,
    val shareAction: String,
    val pngAction: String,
    val useInStudio: String,
    val emptyStyles: String,
    val resetFilters: String,
    val favoritesTitle: String,
    val favoritesEmpty: String,
    val historyTitle: String,
    val historyEmpty: String,
    val templatesTitle: String,
    val templatesSubtitle: String,
    val settingsTitle: String,
    val languageSection: String,
    val themeSection: String,
    val darkModeLabel: String,
    val lightModeLabel: String,
    val storageSection: String,
    val clearFavorites: String,
    val clearHistory: String,
    val confirmClearTitle: String,
    val confirmAction: String,
    val cancelAction: String,
    val shareSheetTitle: String,
    val androidShare: String,
    val pngStudioTitle: String,
    val saveToGallery: String,
    val sharePng: String,
    val includeWatermark: String,
    val emptyTextError: String,
    val copiedToast: String,
    val savedPngToast: String,
    val offlineNotice: String
)

object AppStrings {
    private val english = LocalizedStrings(
        appName = "HELP BOLDIGRAPHI",
        tagline = "Your Text, Your Style!",
        subtitleBilingual = "English Unicode & हिन्दी देवनागरी Studio",
        navStudio = "Studio",
        navTemplates = "Templates",
        navFavorites = "Favorites",
        navHistory = "History",
        navSettings = "Settings",
        inputPlaceholder = "Type in English or हिंदी...",
        searchPlaceholder = "Search styles (Gothic, Mukta, Rozha, Cursive)...",
        previewSizeLabel = "Preview Size",
        copyAction = "Copy",
        copiedAction = "Copied",
        shareAction = "Share",
        pngAction = "PNG",
        useInStudio = "Edit in Studio",
        emptyStyles = "No matching styles found. Try resetting search or script filters.",
        resetFilters = "Reset Filters",
        favoritesTitle = "Saved Favorites",
        favoritesEmpty = "No favorites saved yet. Tap the star icon on any style card to keep it offline.",
        historyTitle = "Recent History",
        historyEmpty = "History is empty. Copy, share, or export any style to track it here.",
        templatesTitle = "Social Media Templates",
        templatesSubtitle = "Ready-to-style Hindi & English bios, statuses, and captions",
        settingsTitle = "Studio Settings",
        languageSection = "App Language",
        themeSection = "Appearance",
        darkModeLabel = "Obsidian Dark Mode",
        lightModeLabel = "Ivory Light Mode",
        storageSection = "Offline Storage",
        clearFavorites = "Clear Favorites",
        clearHistory = "Clear History",
        confirmClearTitle = "Clear saved items?",
        confirmAction = "Clear",
        cancelAction = "Cancel",
        shareSheetTitle = "Share Styled Text",
        androidShare = "Android Share",
        pngStudioTitle = "Text-to-PNG Studio",
        saveToGallery = "Save PNG",
        sharePng = "Share PNG",
        includeWatermark = "Include HELP BOLDIGRAPHI tagline",
        emptyTextError = "Enter some English or Hindi text first.",
        copiedToast = "Copied to clipboard",
        savedPngToast = "PNG saved to Pictures/HELP BOLDIGRAPHI",
        offlineNotice = "100% Offline Ready — All fonts & exports work without internet."
    )

    private val hindi = LocalizedStrings(
        appName = "HELP BOLDIGRAPHI",
        tagline = "आपका टेक्स्ट, आपका स्टाइल!",
        subtitleBilingual = "हिन्दी देवनागरी एवं इंग्लिश यूनिकोड स्टूडियो",
        navStudio = "स्टूडियो",
        navTemplates = "टेम्पलेट्स",
        navFavorites = "पसंदीदा",
        navHistory = "इतिहास",
        navSettings = "सेटिंग्स",
        inputPlaceholder = "यहाँ हिंदी या English में लिखें...",
        searchPlaceholder = "फ़ॉन्ट खोजें (Mukta, Hind, Tiro, Gothic)...",
        previewSizeLabel = "फ़ॉन्ट आकार",
        copyAction = "कॉपी करें",
        copiedAction = "कॉपी हुआ",
        shareAction = "शेयर",
        pngAction = "PNG",
        useInStudio = "स्टूडियो में खोलें",
        emptyStyles = "कोई स्टाइल नहीं मिला। कृपया खोज या भाषा फ़िल्टर रीसेट करें।",
        resetFilters = "फ़िल्टर रीसेट करें",
        favoritesTitle = "पसंदीदा स्टाइल्स",
        favoritesEmpty = "अभी कोई पसंदीदा स्टाइल सेव नहीं है। किसी भी कार्ड पर स्टार दबाकर सेव करें।",
        historyTitle = "हालिया इतिहास",
        historyEmpty = "इतिहास खाली है। कॉपी, शेयर या PNG एक्सपोर्ट करने पर यहाँ रिकॉर्ड दिखेगा।",
        templatesTitle = "सोशल मीडिया टेम्पलेट्स",
        templatesSubtitle = "इंस्टाग्राम बायो, व्हाट्सएप स्टेटस, शायरी और शुभकामना संदेश",
        settingsTitle = "ऐप सेटिंग्स",
        languageSection = "ऐप की भाषा (Language)",
        themeSection = "थीम मोड",
        darkModeLabel = "ओब्सीडियन डार्क मोड",
        lightModeLabel = "आइवरी लाइट मोड",
        storageSection = "ऑफ़लाइन स्टोरेज",
        clearFavorites = "पसंदीदा साफ़ करें",
        clearHistory = "इतिहास साफ़ करें",
        confirmClearTitle = "क्या आप सभी सेव किए गए आइटम हटाना चाहते हैं?",
        confirmAction = "हटाएं",
        cancelAction = "रद्द करें",
        shareSheetTitle = "स्टाइलिश टेक्स्ट शेयर करें",
        androidShare = "सिस्टम शेयर",
        pngStudioTitle = "टेक्स्ट-टू-PNG स्टूडियो",
        saveToGallery = "PNG सेव करें",
        sharePng = "PNG शेयर करें",
        includeWatermark = "HELP BOLDIGRAPHI टैगलाइन जोड़ें",
        emptyTextError = "कृपया पहले कुछ हिंदी या इंग्लिश टेक्स्ट लिखें।",
        copiedToast = "क्लिपबोर्ड पर कॉपी हो गया",
        savedPngToast = "PNG गैलरी में सेव हो गया",
        offlineNotice = "पूर्णतः ऑफ़लाइन सक्षम — बिना इंटरनेट के भी सभी फ़ीचर काम करते हैं।"
    )

    fun forLanguage(language: AppLanguage): LocalizedStrings =
        if (language == AppLanguage.HINDI) hindi else english

    fun scriptFilterLabel(filter: ScriptFilter, lang: AppLanguage): String = when (filter) {
        ScriptFilter.ALL -> if (lang == AppLanguage.HINDI) "सभी" else "All"
        ScriptFilter.ENGLISH -> "English"
        ScriptFilter.HINDI -> "हिन्दी"
    }

    fun categoryLabel(category: StyleCategory, lang: AppLanguage): String = when (category) {
        StyleCategory.ALL -> if (lang == AppLanguage.HINDI) "सभी स्टाइल्स" else "All Styles"
        StyleCategory.DEVANAGARI -> if (lang == AppLanguage.HINDI) "देवनागरी" else "Devanagari"
        StyleCategory.BOLD -> if (lang == AppLanguage.HINDI) "बोल्ड व सेरिफ़" else "Bold & Serif"
        StyleCategory.SCRIPT -> if (lang == AppLanguage.HINDI) "स्क्रिप्ट" else "Script"
        StyleCategory.GOTHIC -> if (lang == AppLanguage.HINDI) "गॉथिक" else "Gothic"
        StyleCategory.DECORATIVE -> if (lang == AppLanguage.HINDI) "सजावटी" else "Decorative"
    }

    fun templateCategoryLabel(category: TemplateCategory, lang: AppLanguage): String = when (category) {
        TemplateCategory.ALL -> if (lang == AppLanguage.HINDI) "सभी" else "All"
        TemplateCategory.INSTAGRAM_BIO -> if (lang == AppLanguage.HINDI) "इंस्टा बायो" else "Insta Bio"
        TemplateCategory.WHATSAPP_STATUS -> if (lang == AppLanguage.HINDI) "व्हाट्सएप स्टेटस" else "WhatsApp"
        TemplateCategory.SHAYARI_QUOTES -> if (lang == AppLanguage.HINDI) "शायरी व कोट्स" else "Shayari"
        TemplateCategory.FESTIVE_WISHES -> if (lang == AppLanguage.HINDI) "शुभकामनाएं" else "Wishes"
        TemplateCategory.CREATOR_HOOKS -> if (lang == AppLanguage.HINDI) "क्रिएटर हुक्स" else "Creators"
    }

    fun renderModeLabel(mode: RenderMode, lang: AppLanguage): String = when (mode) {
        RenderMode.UNICODE -> if (lang == AppLanguage.HINDI) "कॉपी-पेस्ट यूनिकोड" else "Copy-Paste Unicode"
        RenderMode.FONT -> if (lang == AppLanguage.HINDI) "देवनागरी फ़ॉन्ट + PNG" else "Devanagari Font + PNG"
        RenderMode.ORNAMENT -> if (lang == AppLanguage.HINDI) "सजावटी फ़्रेम" else "Ornament Frame"
    }
}
