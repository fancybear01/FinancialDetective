package com.coding.core.domain.model.transactions_models

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType {
    EXPENSE,
    INCOME
}