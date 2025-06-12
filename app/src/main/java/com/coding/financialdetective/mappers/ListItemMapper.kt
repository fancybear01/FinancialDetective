package com.coding.financialdetective.mappers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.financialdetective.models.ContentInfo
import com.coding.financialdetective.models.Expense
import com.coding.financialdetective.models.Income
import com.coding.financialdetective.models.LeadInfo
import com.coding.financialdetective.models.ListItemModel
import com.coding.financialdetective.models.SpendingItem
import com.coding.financialdetective.models.TrailInfo
import com.coding.financialdetective.utils.formatNumberWithSpaces

@Composable
fun Expense.toListItemModel(
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    currencySymbol: String = "₽"
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.emoji,
            containerColor = containerColor
        ),
        content = ContentInfo(
            title = this.category,
            subtitle = this.subcategory
        ),
        trail = TrailInfo.ValueAndChevron(
            value = formatNumberWithSpaces(this.amount) + " " + currencySymbol
        ),
        onClick = {
            TODO()
        }
    )
}

@Composable
fun Income.toListItemModel(
    currencySymbol: String = "₽"
): ListItemModel {
    return ListItemModel(
        lead = null,
        content = ContentInfo(
            title = this.value
        ),
        trail = TrailInfo.ValueAndChevron(
            value = formatNumberWithSpaces(this.amount) + " " + currencySymbol
        ),
        onClick = {
            TODO()
        }
    )
}

@Composable
fun SpendingItem.toListItemModel(
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.emoji,
            containerColor = containerColor
        ),
        content = ContentInfo(
            title = this.name
        ),
        onClick = {
            TODO()
        }
    )
}