package com.coding.core.domain.model.account_models

data class Account(
    val id: String,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)