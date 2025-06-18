package com.coding.financialdetective.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.LeadInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.theme.SwitchColor
import com.coding.financialdetective.ui.theme.White

@Composable
fun ListItem(
    model: ListItemModel,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    addDivider: Boolean = true,
) {
    Column(
        modifier = modifier
            .background(containerColor)
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = model.onClick)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            model.lead?.let { leadInfo ->
                LeadContent(info = leadInfo)
                Spacer(modifier = Modifier.width(16.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Content(info = model.content)
            }

            model.trail?.let { trailInfo ->
                Spacer(modifier = Modifier.width(16.dp))
                TrailContent(trailInfo = trailInfo)
            }
        }
        if (addDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun LeadContent(info: LeadInfo) {
    val isText = info.emoji.all { it.isLetterOrDigit() }

    val fontSize = if (isText) 10.sp else 16.sp
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(info.containerColorForIcon),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = info.emoji,
            fontSize = fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Content(
    info: ContentInfo,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        if (info.subtitle != null) {
            Text(
                text = info.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = info.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Text(
                text = info.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun TrailContent(trailInfo: TrailInfo) {
    when (trailInfo) {
        is TrailInfo.Value -> {
            Column(horizontalAlignment = Alignment.End) {
                if (trailInfo.subtitle != null) {
                    Text(
                        text = trailInfo.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.End,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = trailInfo.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.End,
                    )
                } else {
                    Text(
                        text = trailInfo.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }

        is TrailInfo.ValueAndChevron -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    if (trailInfo.subtitle != null) {
                        Text(
                            text = trailInfo.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.End,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = trailInfo.subtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.End,
                        )
                    } else {
                        Text(
                            text = trailInfo.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.End,
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Icon(
                    painter = painterResource(trailInfo.chevronIcon),
                    contentDescription = null,
                )
            }
        }

        is TrailInfo.Chevron -> {
            Icon(
                painter = painterResource(trailInfo.chevronIcon),
                contentDescription = null,
            )
        }

        is TrailInfo.Switch -> {
            Switch(
                checked = trailInfo.isSwitched,
                onCheckedChange = trailInfo.onSwitch,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.secondary,

                    uncheckedThumbColor = SwitchColor,
                    uncheckedBorderColor = SwitchColor
                )
            )
        }
    }
}