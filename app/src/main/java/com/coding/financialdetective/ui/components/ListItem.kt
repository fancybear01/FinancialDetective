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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.financialdetective.models.ContentInfo
import com.coding.financialdetective.models.LeadInfo
import com.coding.financialdetective.models.ListItemModel
import com.coding.financialdetective.models.TrailInfo
import com.coding.financialdetective.ui.theme.DarkText
import com.coding.financialdetective.ui.theme.Gray
import com.coding.financialdetective.ui.theme.GrayBorder
import com.coding.financialdetective.ui.theme.LightGray
import com.coding.financialdetective.ui.theme.White

@Composable
fun ListItem(
    model: ListItemModel,
    containerColor: Color = White,
    addDivider: Boolean = true,
    modifier: Modifier = Modifier
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
                TrailContent(info = trailInfo)
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
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(info.containerColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = info.emoji,
            fontSize = 16.sp,
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
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        else {
            Text(
                text = info.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
private fun TrailContent(info: TrailInfo) {
    when(info) {
        is TrailInfo.ValueAndChevron -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = info.value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    painter = painterResource(info.chevronIcon),
                    contentDescription = null,
                )
            }
        }
        is TrailInfo.Chevron -> {
            Icon(
                painter = painterResource(info.chevronIcon),
                contentDescription = null,
            )
        }
        is TrailInfo.Switch -> {
            Switch(
                checked = info.isSwitched,
                onCheckedChange = info.onSwitch
            )
        }
    }
}