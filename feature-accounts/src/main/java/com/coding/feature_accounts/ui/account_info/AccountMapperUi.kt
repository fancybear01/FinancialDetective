package com.coding.feature_accounts.ui.account_info

import com.coding.core.util.formatNumberWithSpaces
import com.coding.core.domain.model.account_models.AccountResponse
import com.coding.core.domain.model.account_models.Currency


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