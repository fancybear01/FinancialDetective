package com.coding.financialdetective.ui.screens.account_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.models.Account
import com.coding.financialdetective.models.Income
import com.coding.financialdetective.utils.formatNumberWithSpaces
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            val fakeAccounts = getFakeAccount()
            _state.value = AccountState(
                balance = fakeAccounts.balance
            )
        }
    }

    private fun getFakeAccount(): Account {
        return Account(
            id = "1",
            userId = 1,
            name = "Сберегательный счет",
            balance = formatNumberWithSpaces(6700000.0),
            currency = "RUB",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-01T00:00:00Z",
        )
    }
}