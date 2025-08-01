package com.coding.feature_accounts.ui.editing_an_account

import com.coding.core.util.UiText
import com.coding.core.domain.model.account_models.Currency

/**
 * Состояние экрана редактирования счёта.
 * Содержит все данные, необходимые для отображения UI экрана аккаунта.
 */
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