package com.coding.financialdetective.models.domain_models

data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)