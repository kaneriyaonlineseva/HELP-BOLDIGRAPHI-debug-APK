package com.helpboldigraphi.app.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.PngAspectRatio
import com.helpboldigraphi.app.domain.model.PngThemePreset
import java.io.File
import java.io.FileOutputStream

enum class ShareTargetPackage(val packageName: String, val displayName: String) {
    WHATSAPP("com.whatsapp", "WhatsApp"),
    FACEBOOK("com.facebook.katana", "Facebook"),
    INSTAGRAM("com.instagram.android", "Instagram"),
    TELEGRAM("org.telegram.messenger", "Telegram")
}

sealed interface PlatformResult {
    data object Success : PlatformResult
    data class Fallback(val message: String) : PlatformResult
    data class Error(val message: String) : PlatformResult
}

object AndroidShareHelper {

    fun copyToClipboard(context: Context, label: String, text: String): PlatformResult {
        if (text.isBlank()) {
            return PlatformResult.Error("Cannot copy empty text.")
        }
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
            PlatformResult.Success
        } catch (e: Exception) {
            PlatformResult.Error(e.localizedMessage ?: "Clipboard unavailable.")
        }
    }

    fun shareTextSystem(context: Context, text: String): PlatformResult {
        if (text.isBlank()) {
            return PlatformResult.Error("Cannot share empty text.")
        }
        return try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            val chooser = Intent.createChooser(intent, "Share via HELP BOLDIGRAPHI").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
            PlatformResult.Success
        } catch (e: Exception) {
            PlatformResult.Error(e.localizedMessage ?: "Unable to launch system share.")
        }
    }

    fun shareTextToPlatform(
        context: Context,
        text: String,
        target: ShareTargetPackage
    ): PlatformResult {
        if (text.isBlank()) {
            return PlatformResult.Error("Cannot share empty text.")
        }
        val directIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            setPackage(target.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            if (directIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(directIntent)
                PlatformResult.Success
            } else {
                copyToClipboard(context, "HELP BOLDIGRAPHI", text)
                shareTextSystem(context, text)
                PlatformResult.Fallback("${target.displayName} not found — copied text and opened chooser.")
            }
        } catch (_: Exception) {
            copyToClipboard(context, "HELP BOLDIGRAPHI", text)
            shareTextSystem(context, text)
            PlatformResult.Fallback("Copied text and opened system share.")
        }
    }

    fun renderPngBitmap(
        context: Context,
        text: String,
        styleName: String,
        preset: PngThemePreset,
        aspectRatio: PngAspectRatio,
        fontSizeSp: Float,
        showWatermark: Boolean,
        fontType: BundledFontType,
        useBold: Boolean
    ): Bitmap {
        val width = aspectRatio.width
        val height = aspectRatio.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                preset.bgStartArgb,
                preset.bgEndArgb,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val inset = 44f
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = preset.borderArgb
        }
        canvas.drawRoundRect(
            RectF(inset, inset, width - inset, height - inset),
            32f, 32f, borderPaint
        )

        val fontRes = if (useBold) fontType.boldRes else fontType.regularRes
        val loadedTypeface = runCatching { ResourcesCompat.getFont(context, fontRes) }.getOrNull()
            ?: Typeface.DEFAULT

        val safeText = text.ifBlank { "HELP BOLDIGRAPHI" }
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = preset.textArgb
            textSize = fontSizeSp * (width / 540f)
            typeface = loadedTypeface
        }

        val maxWidth = (width - 220).coerceAtLeast(300)
        val staticLayout = StaticLayout.Builder
            .obtain(safeText, 0, safeText.length, textPaint, maxWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1.55f)
            .setIncludePad(true)
            .build()

        canvas.save()
        val textY = ((height - staticLayout.height) / 2f).coerceAtLeast(inset + 60f)
        canvas.translate((width - maxWidth) / 2f, textY)
        staticLayout.draw(canvas)
        canvas.restore()

        if (showWatermark) {
            val brandTypeface = runCatching {
                ResourcesCompat.getFont(context, BundledFontType.SPACE_GROTESK.regularRes)
            }.getOrNull() ?: Typeface.DEFAULT_BOLD

            val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = preset.accentArgb
                textSize = 24f
                textAlign = Paint.Align.CENTER
                typeface = brandTypeface
            }
            canvas.drawText("HELP BOLDIGRAPHI", width / 2f, height - inset - 68f, brandPaint)

            val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = preset.mutedArgb
                textSize = 18f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("Your Text, Your Style!  ·  $styleName", width / 2f, height - inset - 34f, subPaint)
        }

        return bitmap
    }

    fun savePngToGallery(context: Context, bitmap: Bitmap): Result<Uri> = runCatching {
        val filename = "help_boldigraphi_${System.currentTimeMillis()}.png"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/HELP BOLDIGRAPHI")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: error("Unable to create MediaStore entry")
            resolver.openOutputStream(uri)?.use { out ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    error("Failed to compress PNG")
                }
            } ?: error("Unable to open gallery output stream")
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            uri
        } else {
            @Suppress("DEPRECATION")
            val picturesDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "HELP BOLDIGRAPHI"
            ).apply { mkdirs() }
            val outFile = File(picturesDir, filename)
            FileOutputStream(outFile).use { out ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    error("Failed to compress PNG")
                }
            }
            Uri.fromFile(outFile)
        }
    }

    fun exportAndSharePng(
        context: Context,
        bitmap: Bitmap,
        targetPackage: ShareTargetPackage? = null
    ): Result<Uri> = runCatching {
        val exportsDir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(exportsDir, "boldigraphi_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                error("Failed to encode PNG")
            }
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (targetPackage != null) {
                setPackage(targetPackage.packageName)
            }
        }
        val launchIntent = if (targetPackage != null && shareIntent.resolveActivity(context.packageManager) != null) {
            shareIntent
        } else {
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                },
                "Share PNG Image"
            )
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
        uri
    }
}
