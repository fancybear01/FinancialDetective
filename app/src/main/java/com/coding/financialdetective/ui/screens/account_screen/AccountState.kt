package com.coding.financialdetective.ui.screens.account_screen

import com.coding.financialdetective.core.domain.util.UiText

data class AccountState(
    val isLoading: Boolean = false,
    val accountName: String = "Мой счёт",
    val balance: String = "0",
    val currency: String = "₽",
    val error: UiText? = null,
    val userMessages: List<String> = emptyList()
)