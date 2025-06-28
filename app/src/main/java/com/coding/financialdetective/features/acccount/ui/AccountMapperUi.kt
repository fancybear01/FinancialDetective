package com.coding.financialdetective.features.acccount.ui

import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse


fun AccountResponse.toUiModel(): AccountUi {
    return AccountUi(
        id = this.id,
        name = this.name,
        balance = formatNumberWithSpaces(this.balance),
        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}