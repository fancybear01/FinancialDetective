package com.coding.financialdetective.models.domain_models

data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)