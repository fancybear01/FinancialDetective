package com.coding.core_ui.model.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core.domain.model.account_models.Currency
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.LeadInfo
import com.coding.core_ui.common.list_item.ListItemModel
import java.time.format.DateTimeFormatter
import com.coding.core_ui.model.TransactionUi
import java.util.Locale

fun Transaction.toUiModel(): TransactionUi {
    val currency = Currency.fromCode(this.account.currency)
    return TransactionUi(
        id = this.id.toString(),
        categoryName = this.category.name,
        categoryEmoji = this.category.emoji,
        currency = currency.symbol,
        comment = this.comment,
        formattedAmount = formatNumberWithSpaces(this.amount),
        formattedDate = this.transactionDate.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(
                Locale("ru")
            )
        )
    )
}

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
        trail = com.coding.core_ui.common.list_item.TrailInfo.ValueAndChevron(
            title = "${this.formattedAmount} ${this.currency}",
            subtitle = if (showDate) this.formattedDate else null,
        )
    )
}