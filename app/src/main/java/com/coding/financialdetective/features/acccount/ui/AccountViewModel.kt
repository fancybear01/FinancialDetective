package com.coding.financialdetective.features.acccount.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository,
    private val accountId: String
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    init {
        loadAccountById()
    }

    private fun loadAccountById() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository
                .getAccountById(accountId)
                .onSuccess { accountResponse ->
                    val account = accountResponse.toUiModel()
                    _state.update {
                        it.copy(
                            accountName = account.name,
                            balance = account.balance,
                            currency = account.currency,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onError { networkError ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = networkError.toUiText()
                        )
                    }
                }
        }
    }

    fun retry() {
        loadAccountById()
    }
}