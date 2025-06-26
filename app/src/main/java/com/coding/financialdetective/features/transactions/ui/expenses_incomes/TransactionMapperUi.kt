package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import java.time.format.DateTimeFormatter
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi
import java.util.Locale

fun Transaction.toUiModel(): TransactionUi {
    return TransactionUi(
        id = this.id,
        categoryName = this.category.name,
        categoryEmoji = this.category.emoji,
        comment = this.comment,
        formattedAmount = formatNumberWithSpaces(this.amount) + " " + this.account.currency,
        formattedDate = this.transactionDate.format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withLocale(
                Locale("ru")
            )
        )
    )
}