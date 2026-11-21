package com.helpboldigraphi.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helpboldigraphi.app.domain.model.AppLanguage
import com.helpboldigraphi.app.domain.model.SocialTemplate
import com.helpboldigraphi.app.domain.model.TemplateCategory
import com.helpboldigraphi.app.ui.theme.AppStrings
import com.helpboldigraphi.app.ui.theme.FontFamilyRegistry
import com.helpboldigraphi.app.ui.theme.LocalizedStrings

@Composable
fun TemplatesScreen(
    templates: List<SocialTemplate>,
    selectedCategory: TemplateCategory,
    language: AppLanguage,
    strings: LocalizedStrings,
    onCategoryChange: (TemplateCategory) -> Unit,
    onCopy: (SocialTemplate) -> Unit,
    onShare: (SocialTemplate) -> Unit,
    onExportPng: (SocialTemplate) -> Unit,
    onUseInStudio: (SocialTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(strings.templatesTitle, style = MaterialTheme.typography.titleLarge)
                Text(
                    strings.templatesSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TemplateCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(AppStrings.templateCategoryLabel(category, language)) }
                    )
                }
            }
        }

        items(templates, key = { it.id }) { tpl ->
            val title = if (language == AppLanguage.HINDI) tpl.titleHi else tpl.titleEn
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = AppStrings.templateCategoryLabel(tpl.category, language),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = tpl.body,
                            fontFamily = FontFamilyRegistry.resolve(tpl.fontType),
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onCopy(tpl) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.copyAction)
                        }
                        OutlinedButton(
                            onClick = { onShare(tpl) },
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = strings.shareAction, modifier = Modifier.size(16.dp))
                        }
                        OutlinedButton(
                            onClick = { onExportPng(tpl) },
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Icon(
                                Icons.Filled.Image,
                                contentDescription = strings.pngAction,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        OutlinedButton(
                            onClick = { onUseInStudio(tpl) },
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = strings.useInStudio, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
