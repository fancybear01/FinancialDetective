package com.coding.financialdetective.features.acccount.ui.account_info

data class AccountUi(
    val id: String,
    val name: String,
    val balance: String,
    val rawBalance: Double,
    val currency: String,
    val currencyCode: String,
    val createdAt: String,
    val updatedAt: String
)