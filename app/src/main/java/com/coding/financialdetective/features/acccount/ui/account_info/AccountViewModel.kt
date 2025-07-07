package com.coding.financialdetective.features.acccount.ui.account_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.di.AccountId
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для основного экрана с счётом.
 *
 * @param repository Репозиторий для доступа к данным счетов
 * @param accountId Уникальный идентификатор редактируемого счёта
 * @param connectivityObserver Наблюдатель за состоянием сетевого подключения
 */
class AccountViewModel @AssistedInject constructor(
    private val repository: AccountRepository,
    @Assisted private val accountId: String,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String): AccountViewModel
    }

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    init {
        loadAccountById()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.isConnected
                .drop(1)
                .debounce(1000)
                .collect { connected ->
                    if (connected && state.value.error != null) {
                        retry()
                    }
                }
        }
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

    fun refresh() {
        loadAccountById()
    }
}