package com.coding.financialdetective.features.acccount.ui

import com.coding.financialdetective.core_ui.util.UiText


data class AccountState(
    val isLoading: Boolean = false,
    val accountName: String = "Мой счёт",
    val balance: String = "0",
    val currency: String = "₽",
    val error: UiText? = null,
    val userMessages: List<String> = emptyList()
)