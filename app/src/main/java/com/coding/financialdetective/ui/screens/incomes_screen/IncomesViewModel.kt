package com.coding.financialdetective.ui.screens.incomes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.models.domain_models.Income
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IncomesViewModel : ViewModel() {
    private val _state = MutableStateFlow(IncomesState())
    val state: StateFlow<IncomesState> = _state.asStateFlow()

    init {
        loadIncomes()
    }

    private fun loadIncomes() {
        viewModelScope.launch {
            val fakeIncomes = getFakeIncomes()
            val total = fakeIncomes.sumOf { it.amount }

            _state.value = IncomesState(
                totalAmount = total,
                incomes = fakeIncomes,
                isLoading = false
            )
        }
    }

    private fun getFakeIncomes(): List<Income> {
        return listOf(
            Income(
                id = "1",
                value = "Зарплата",
                amount = 50000.0
            ),
            Income(
                id = "2",
                value = "Подработка",
                amount = 25000.0
            ),
        )
    }
}