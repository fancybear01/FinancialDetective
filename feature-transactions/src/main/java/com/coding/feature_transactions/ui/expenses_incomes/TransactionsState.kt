package com.coding.feature_transactions.ui.expenses_incomes

import com.coding.core.util.UiText
import com.coding.feature_transactions.ui.model.TransactionUi

data class TransactionsState(
    val totalAmount: Double = 0.0,
    val transactions: List<TransactionUi> = emptyList(),
    val currency: String = "",
    val isLoading: Boolean = true,
    val error: UiText? = null,
)