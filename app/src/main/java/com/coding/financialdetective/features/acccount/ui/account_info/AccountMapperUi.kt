package com.coding.financialdetective.features.acccount.ui.account_info

import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse
import com.coding.financialdetective.features.acccount.domain.model.Currency


fun AccountResponse.toUiModel(): AccountUi {
    val currency = Currency.fromCode(this.currency)
    return AccountUi(
        id = this.id,
        name = this.name,
        balance = formatNumberWithSpaces(this.balance),
        rawBalance = this.balance,
        currency = currency.symbol,
        currencyCode = currency.code,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}