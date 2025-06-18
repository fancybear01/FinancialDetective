package com.coding.financialdetective.models.domain_models

import java.time.ZonedDateTime

data class Transaction(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: ZonedDateTime,
    val comment: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)