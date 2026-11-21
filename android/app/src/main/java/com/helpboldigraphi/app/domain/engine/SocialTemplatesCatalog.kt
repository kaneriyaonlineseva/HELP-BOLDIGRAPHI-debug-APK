package com.helpboldigraphi.app.domain.engine

import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.ScriptKind
import com.helpboldigraphi.app.domain.model.SocialTemplate
import com.helpboldigraphi.app.domain.model.TemplateCategory

object SocialTemplatesCatalog {
    val templates: List<SocialTemplate> = listOf(
        SocialTemplate(
            id = "tpl-insta-1",
            titleEn = "Creator Luxury Bio",
            titleHi = "क्रिएटर लक्ज़री बायो",
            category = TemplateCategory.INSTAGRAM_BIO,
            script = ScriptKind.ENGLISH,
            fontType = BundledFontType.SYNE_DISPLAY,
            body = "♛ 𝐂𝐫𝐞𝐚𝐭𝐢𝐯𝐞 𝐃𝐢𝐫𝐞𝐜𝐭𝐨𝐫 ♛\n✦ Design · Typography · Visuals\n『 Your Text, Your Style! 』"
        ),
        SocialTemplate(
            id = "tpl-insta-2",
            titleEn = "Hindi Royal Bio",
            titleHi = "हिन्दी रॉयल बायो",
            category = TemplateCategory.INSTAGRAM_BIO,
            script = ScriptKind.HINDI,
            fontType = BundledFontType.ROZHA_ONE,
            body = "꧁❖ शब्दों का जादूगर ❖꧂\n✦ कला · संस्कृति · सृजन\n॥ कर्म ही पहचान है ॥"
        ),
        SocialTemplate(
            id = "tpl-wa-1",
            titleEn = "Morning Positivity",
            titleHi = "सुप्रभात प्रेरणा",
            category = TemplateCategory.WHATSAPP_STATUS,
            script = ScriptKind.HINDI,
            fontType = BundledFontType.MUKTA,
            body = "❀⋆｡ सुप्रभात ｡⋆❀\nनया दिन, नई उम्मीद और नया विश्वास।\nआपका दिन मंगलमय हो!"
        ),
        SocialTemplate(
            id = "tpl-wa-2",
            titleEn = "Focus & Hustle Status",
            titleHi = "फोकस और मेहनत स्टेटस",
            category = TemplateCategory.WHATSAPP_STATUS,
            script = ScriptKind.ENGLISH,
            fontType = BundledFontType.SPACE_GROTESK,
            body = "𝗕𝘂𝗶𝗹𝗱 𝗶𝗻 𝘀𝗶𝗹𝗲𝗻𝗰𝗲.\n𝗟𝗲𝘁 𝘆𝗼𝘂𝗿 𝗰𝗿𝗮𝗳𝘁 𝘀𝗽𝗲𝗮𝗸."
        ),
        SocialTemplate(
            id = "tpl-shayari-1",
            titleEn = "Classic Hindi Shayari",
            titleHi = "हिन्दी साहित्य शायरी",
            category = TemplateCategory.SHAYARI_QUOTES,
            script = ScriptKind.HINDI,
            fontType = BundledFontType.TIRO_DEVANAGARI,
            body = "❝ शब्दों की अपनी कलाकारी है,\nख़ामोशी में भी एक कहानी भारी है। ❞"
        ),
        SocialTemplate(
            id = "tpl-shayari-2",
            titleEn = "Bilingual Attitude Quote",
            titleHi = "द्विभाषी मोटिवेशन कोट",
            category = TemplateCategory.SHAYARI_QUOTES,
            script = ScriptKind.BOTH,
            fontType = BundledFontType.HIND,
            body = "✦ खुद पर भरोसा रखो ✦\n𝔜𝔬𝔲𝔯 𝔳𝔦𝔰𝔦𝔬𝔫 𝔦𝔰 𝔶𝔬𝔲𝔯 𝔰𝔲𝔭𝔢𝔯𝔭𝔬𝔴𝔢𝔯."
        ),
        SocialTemplate(
            id = "tpl-festive-1",
            titleEn = "Festive Celebration",
            titleHi = "त्योहार शुभकामनाएं",
            category = TemplateCategory.FESTIVE_WISHES,
            script = ScriptKind.HINDI,
            fontType = BundledFontType.ROZHA_ONE,
            body = "✦ हार्दिक शुभकामनाएं २०२६ ✦\nआपके जीवन में सुख, शांति और समृद्धि का वास हो।"
        ),
        SocialTemplate(
            id = "tpl-festive-2",
            titleEn = "Milestone Congrats",
            titleHi = "बधाई संदेश",
            category = TemplateCategory.FESTIVE_WISHES,
            script = ScriptKind.ENGLISH,
            fontType = BundledFontType.SYNE_DISPLAY,
            body = "⋆｡°✩ 𝒞𝑜𝓃𝑔𝓇𝒶𝓉𝓊𝓁𝒶𝓉𝒾𝑜𝓃𝓈! ✩°｡⋆\nWishing you unstoppable growth & brilliance."
        ),
        SocialTemplate(
            id = "tpl-creator-1",
            titleEn = "Reel / Shorts Hook",
            titleHi = "रील / शॉर्ट्स हुक",
            category = TemplateCategory.CREATOR_HOOKS,
            script = ScriptKind.BOTH,
            fontType = BundledFontType.HIND,
            body = "🔥 𝐓𝐨𝐩 𝟑 𝐓𝐲𝐩𝐨𝐠𝐫𝐚𝐩𝐡𝐲 𝐒𝐞𝐜𝐫𝐞𝐭𝐬\nइसे सेव करें और अपने दोस्तों को शेयर करें!"
        ),
        SocialTemplate(
            id = "tpl-creator-2",
            titleEn = "YouTube Thumbnail Title",
            titleHi = "यूट्यूब थंबनेल शीर्षक",
            category = TemplateCategory.CREATOR_HOOKS,
            script = ScriptKind.HINDI,
            fontType = BundledFontType.NOTO_SANS_DEVANAGARI,
            body = "अब हर पोस्ट दिखेगी प्रोफ़ेशनल — सिर्फ़ १ क्लिक में!"
        )
    )
}
