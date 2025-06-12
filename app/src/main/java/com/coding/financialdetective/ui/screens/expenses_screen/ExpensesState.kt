package com.coding.financialdetective.ui.screens.expenses_screen

import com.coding.financialdetective.models.Expense

data class ExpensesState(
    val totalAmount: Double = 0.0,
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true
)