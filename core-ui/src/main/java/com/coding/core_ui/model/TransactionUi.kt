package com.coding.core_ui.model

data class TransactionUi(
    val id: String,
    val categoryName: String,
    val categoryEmoji: String,
    val currency: String,
    val comment: String,
    val formattedAmount: String,
    val formattedDate: String,
)