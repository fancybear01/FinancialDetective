package com.coding.financialdetective.ui.screens.incomes_screen

import com.coding.financialdetective.models.Expense
import com.coding.financialdetective.models.Income

data class IncomesState(
    val totalAmount: Double = 0.0,
    val incomes: List<Income> = emptyList(),
    val isLoading: Boolean = true
)