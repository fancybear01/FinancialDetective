package com.coding.feature_accounts.ui.account_info

import com.coding.core.util.UiText

/**
 * Состояние экрана счёта.
 * Содержит все данные, необходимые для отображения UI экрана аккаунта.
 */
data class AccountState(
    val id: String = "",
    val isLoading: Boolean = false,
    val accountName: String = "Мой счёт",
    val balance: String = "0",
    val rawBalance: Double = 0.0,
    val currency: String = "₽",
    val currencyCode: String = "RUB",
    val error: UiText? = null,
    val userMessages: List<String> = emptyList()
)