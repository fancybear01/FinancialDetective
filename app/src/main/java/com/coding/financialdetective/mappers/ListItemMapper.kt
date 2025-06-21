package com.coding.financialdetective.mappers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.LeadInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.models.ui_models.CategoryUi
import com.coding.financialdetective.models.ui_models.TransactionUi

@Composable
fun TransactionUi.toListItemModel(
    containerColorForIcon: Color = MaterialTheme.colorScheme.secondary,
    showDate: Boolean = true
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.categoryEmoji,
            containerColorForIcon = containerColorForIcon
        ),
        content = ContentInfo(
            title = this.categoryName,
            subtitle = this.comment
        ),
        trail = TrailInfo.ValueAndChevron(
            title = this.formattedAmount,
            subtitle = if (showDate) this.formattedDate else null,
        ),
        onClick = {
            /* TODO() */
        }
    )
}

@Composable
fun CategoryUi.toListItemModel(
    containerColorForIcon: Color = MaterialTheme.colorScheme.secondary
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.emoji,
            containerColorForIcon = containerColorForIcon
        ),
        content = ContentInfo(
            title = this.name
        ),
        onClick = {
            /* TODO() */
        }
    )
}