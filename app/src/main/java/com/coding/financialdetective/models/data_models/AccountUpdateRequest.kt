package com.coding.financialdetective.models.data_models

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)