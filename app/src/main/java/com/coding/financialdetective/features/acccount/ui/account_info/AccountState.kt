package com.coding.financialdetective.features.acccount.ui.account_info

import com.coding.financialdetective.core_ui.util.UiText


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