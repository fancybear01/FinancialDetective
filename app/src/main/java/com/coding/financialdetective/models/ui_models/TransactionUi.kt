package com.coding.financialdetective.models.ui_models

data class TransactionUi(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val comment: String,
    val formattedAmount: String,
    val formattedDate: String,
)