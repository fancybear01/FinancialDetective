package com.coding.core.domain.model.transactions_models

import com.coding.core.domain.model.account_models.AccountBrief
import com.coding.core.domain.model.categories_models.Category
import java.time.ZonedDateTime

data class Transaction(
    val id: String,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: ZonedDateTime,
    val comment: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)