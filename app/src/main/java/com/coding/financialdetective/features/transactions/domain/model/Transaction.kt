package com.coding.financialdetective.features.transactions.domain.model

import com.coding.financialdetective.features.acccount.domain.model.AccountBrief
import com.coding.financialdetective.features.categories.domain.model.Category
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