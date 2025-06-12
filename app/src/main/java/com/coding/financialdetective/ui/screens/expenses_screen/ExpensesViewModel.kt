package com.coding.financialdetective.ui.screens.expenses_screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.R
import com.coding.financialdetective.models.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpensesViewModel : ViewModel() {
    private val _state = MutableStateFlow(ExpensesState())
    val state: StateFlow<ExpensesState> = _state.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            val fakeExpenses = getFakeExpenses()
            val total = fakeExpenses.sumOf { it.amount }

            _state.value = ExpensesState(
                totalAmount = total,
                expenses = fakeExpenses,
                isLoading = false
            )
        }
    }

    fun getFakeExpenses(): List<Expense> {
        return listOf(
            Expense("1", "Аренда квартиры", "Дом", 100000.0, "\uD83C\uDFE1"),
            Expense("2", "Одежда", null, 15000.0, "\uD83D\uDC57"),
            Expense("3", "На собаку", "Дом", 7500.0, "\uD83D\uDC36"),
            Expense("4", "Спортзал", "Вики", 8000.0, "\uD83C\uDFCB\uFE0F\u200D♂\uFE0F"),
            Expense("5", "Медицина", null, 50000.0, "\uD83D\uDC8A"),
            Expense("6", "Продукты", "Супермаркет", 25000.0, "\uD83C\uDF6D"),
        )
    }
}