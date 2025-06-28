package com.coding.financialdetective.features.transactions.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType {
    EXPENSE,
    INCOME
}