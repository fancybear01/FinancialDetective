package com.coding.financialdetective.models.data_models

data class AccountCreateRequest(
    val name: String,
    val balance: String,
    val currency: String
)