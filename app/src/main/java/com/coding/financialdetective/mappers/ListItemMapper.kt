package com.coding.financialdetective.mappers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.domain_models.Expense
import com.coding.financialdetective.models.domain_models.Income
import com.coding.financialdetective.models.ui_models.LeadInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.domain_models.CategoryModelOld
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.models.ui_models.TransactionUi

@Composable
fun Expense.toListItemModel(
    containerColor: Color = MaterialTheme.colorScheme.secondary,
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
            title = formatNumberWithSpaces(this.amount) + " " + currencySymbol
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
            title = formatNumberWithSpaces(this.amount) + " " + currencySymbol
        ),
        onClick = {
            TODO()
        }
    )
}

@Composable
fun CategoryModelOld.toListItemModel(
    containerColor: Color = MaterialTheme.colorScheme.secondary,
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

@Composable
fun TransactionUi.toListItemModel(
    containerColor: Color = MaterialTheme.colorScheme.secondary
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.categoryEmoji,
            containerColor = containerColor
        ),
        content = ContentInfo(
            title = this.comment
        ),
        trail = TrailInfo.ValueAndChevron(
            title = this.formattedAmount,
            subtitle = this.formattedDate
        ),
        onClick = {
            TODO()
        }
    )
}