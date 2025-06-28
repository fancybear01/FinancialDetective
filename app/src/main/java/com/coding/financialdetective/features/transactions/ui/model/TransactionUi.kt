package com.coding.financialdetective.features.transactions.ui.model

data class TransactionUi(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val comment: String,
    val formattedAmount: String,
    val formattedDate: String,
)