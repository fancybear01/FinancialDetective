package com.coding.financialdetective.core_ui.common.list_item

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.financialdetective.features.categories.ui.CategoryUi
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi

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
            title = "${this.formattedAmount} ${this.currency}",
            subtitle = if (showDate) this.formattedDate else null,
        )
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
        )
    )
}