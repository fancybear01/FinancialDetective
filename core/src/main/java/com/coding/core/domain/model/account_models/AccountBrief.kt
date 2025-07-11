package com.coding.core.domain.model.account_models

data class AccountBrief(
    val id: String,
    val name: String,
    val balance: Double,
    val currency: String
)