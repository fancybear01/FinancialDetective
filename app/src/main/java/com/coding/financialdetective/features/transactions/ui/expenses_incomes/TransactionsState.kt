package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import com.coding.financialdetective.core_ui.util.UiText
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi

data class TransactionsState(
    val totalAmount: Double = 0.0,
    val transactions: List<TransactionUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiText? = null,
)