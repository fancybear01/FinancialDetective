package com.coding.financialdetective.ui.screens.expenses_incomes_screens

import com.coding.financialdetective.models.ui_models.TransactionUi

data class TransactionsState(
    val totalAmount: Double = 0.0,
    val transactions: List<TransactionUi> = emptyList(),
    val isLoading: Boolean = true
)