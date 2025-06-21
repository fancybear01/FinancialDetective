package com.coding.financialdetective.models.ui_models

data class AccountUi(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)