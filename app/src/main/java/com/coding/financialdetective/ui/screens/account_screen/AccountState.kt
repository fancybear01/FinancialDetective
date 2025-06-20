package com.coding.financialdetective.ui.screens.account_screen

data class AccountState(
    val isLoading: Boolean = false,
    val accountName: String = "Мой счёт",
    val balance: String = "0",
    val currency: String = "₽",
    val error: String? = null
)