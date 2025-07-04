package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import android.util.Log
import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.features.acccount.domain.model.Currency
import com.coding.financialdetective.features.acccount.ui.account_info.toUiModel
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import java.time.format.DateTimeFormatter
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi
import java.util.Locale

fun Transaction.toUiModel(): TransactionUi {
    val currency = Currency.fromCode(this.account.currency)
    Log.d("CURRENCY", "Current currency ${currency.symbol}")
    return TransactionUi(
        id = this.id,
        categoryName = this.category.name,
        categoryEmoji = this.category.emoji,
        currency = currency.symbol,
        comment = this.comment,
        formattedAmount = formatNumberWithSpaces(this.amount),
        formattedDate = this.transactionDate.format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withLocale(
                Locale("ru")
            )
        )
    )
}