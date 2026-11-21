package com.helpboldigraphi.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.ScriptKind
import com.helpboldigraphi.app.domain.model.StylePreset
import com.helpboldigraphi.app.ui.theme.AppStrings
import com.helpboldigraphi.app.ui.theme.FontFamilyRegistry
import com.helpboldigraphi.app.ui.theme.LocalizedStrings

@Composable
fun StyleCard(
    preset: StylePreset,
    renderedText: String,
    previewFontSizeSp: Float,
    language: AppLanguage,
    strings: LocalizedStrings,
    isCopied: Boolean,
    isFavorited: Boolean,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onExportPng: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isCopied) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
    val previewFontFamily = FontFamilyRegistry.resolve(preset.fontType)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = preset.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (preset.isVip) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Featured",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    val scriptLabel = when (preset.script) {
                        ScriptKind.HINDI -> "हिन्दी Devanagari"
                        ScriptKind.ENGLISH -> "English Unicode"
                        ScriptKind.BOTH -> "English & हिन्दी"
                    }
                    Text(
                        text = "$scriptLabel · ${AppStrings.renderModeLabel(preset.renderMode, language)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = if (isFavorited) "Remove favorite" else "Save favorite",
                        tint = if (isFavorited) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = renderedText,
                    fontSize = previewFontSizeSp.sp,
                    lineHeight = (previewFontSizeSp * 1.65f).sp,
                    fontFamily = previewFontFamily,
                    fontWeight = if (preset.useBoldWeight) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCopy,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCopied) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        contentColor = if (isCopied) Color(0xFF1A1100) else Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isCopied) strings.copiedAction else strings.copyAction)
                }

                OutlinedButton(
                    onClick = onShare,
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = strings.shareAction, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.shareAction)
                }

                OutlinedButton(
                    onClick = onExportPng,
                    shape = RoundedCornerShape(999.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = strings.pngAction,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.pngAction)
                }
            }
        }
    }
}
