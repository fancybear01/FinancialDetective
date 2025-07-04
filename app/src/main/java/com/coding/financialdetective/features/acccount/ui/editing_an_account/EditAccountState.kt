package com.coding.financialdetective.features.acccount.ui.editing_an_account

import com.coding.financialdetective.core_ui.util.UiText
import com.coding.financialdetective.features.acccount.domain.model.Currency

data class EditAccountState(
    val accountName: String = "",
    val balance: String = "0",
    val rawBalance: Double = 0.0,
    val selectedCurrency: Currency = Currency.RUB,
    val currencySymbol: String = "₽",
    val isSaveEnabled: Boolean = accountName.isNotBlank(),
    val hasChanges: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null
)