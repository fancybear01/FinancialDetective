package com.coding.financialdetective.features.acccount.domain.model

data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)