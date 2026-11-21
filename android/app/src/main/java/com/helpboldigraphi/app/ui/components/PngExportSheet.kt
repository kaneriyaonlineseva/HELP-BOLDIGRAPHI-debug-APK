package com.helpboldigraphi.app.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.helpboldigraphi.app.domain.model.BundledFontType
import com.helpboldigraphi.app.domain.model.PngAspectRatio
import com.helpboldigraphi.app.domain.model.PngThemePreset
import com.helpboldigraphi.app.platform.AndroidShareHelper
import com.helpboldigraphi.app.ui.theme.LocalizedStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PngExportSheet(
    text: String,
    styleName: String,
    fontType: BundledFontType,
    useBold: Boolean,
    strings: LocalizedStrings,
    onDismiss: () -> Unit,
    onSaveToGallery: (Bitmap) -> Unit,
    onShareBitmap: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    var selectedPreset by remember { mutableStateOf(PngThemePreset.OBSIDIAN_VIOLET) }
    var selectedRatio by remember { mutableStateOf(PngAspectRatio.SQUARE) }
    var fontSizeSp by remember { mutableFloatStateOf(32f) }
    var showWatermark by remember { mutableStateOf(true) }

    val previewBitmap = remember(
        text,
        styleName,
        selectedPreset,
        selectedRatio,
        fontSizeSp,
        showWatermark,
        fontType,
        useBold
    ) {
        AndroidShareHelper.renderPngBitmap(
            context = context,
            text = text,
            styleName = styleName,
            preset = selectedPreset,
            aspectRatio = selectedRatio,
            fontSizeSp = fontSizeSp,
            showWatermark = showWatermark,
            fontType = fontType,
            useBold = useBold
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(strings.pngStudioTitle, style = MaterialTheme.typography.titleLarge)
            Text(
                styleName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Image(
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = "PNG preview",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PngThemePreset.entries.forEach { preset ->
                    FilterChip(
                        selected = selectedPreset == preset,
                        onClick = { selectedPreset = preset },
                        label = { Text(preset.displayName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PngAspectRatio.entries.forEach { ratio ->
                    FilterChip(
                        selected = selectedRatio == ratio,
                        onClick = { selectedRatio = ratio },
                        label = { Text(ratio.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("${strings.previewSizeLabel}: ${fontSizeSp.toInt()}pt", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = fontSizeSp,
                onValueChange = { fontSizeSp = it },
                valueRange = 20f..56f
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = showWatermark, onCheckedChange = { showWatermark = it })
                Text(strings.includeWatermark, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onSaveToGallery(previewBitmap) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(strings.saveToGallery)
                }
                Button(
                    onClick = { onShareBitmap(previewBitmap) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(strings.sharePng)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
