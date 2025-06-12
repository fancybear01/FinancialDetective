package com.coding.financialdetective.models

data class Account(
    val id: String,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)