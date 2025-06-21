package com.coding.financialdetective.ui.screens.account_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteAccountDataSource
import com.coding.financialdetective.core.domain.repositories.AccountDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.core.presentation.util.toUiText
import com.coding.financialdetective.mappers.toUiModel
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(private val accountId: String) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val accountDataSource: AccountDataSource = RemoteAccountDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        loadAccountById()
    }

    private fun loadAccountById() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            accountDataSource
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

class AccountViewModelFactory(private val accountId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(accountId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}