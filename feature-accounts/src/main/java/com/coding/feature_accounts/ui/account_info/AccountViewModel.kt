package com.coding.feature_accounts.ui.account_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.repository.AccountRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val refreshTrigger = MutableStateFlow(0)

    init {
        observeForUpdates()
        observeConnectivity()
    }

    private fun observeForUpdates() {
        viewModelScope.launch {
            refreshTrigger.collect {
                loadAccountDetails()
            }
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .drop(1).debounce(1000)
            .filter { it && state.value.error != null }
            .onEach { retry() }
            .launchIn(viewModelScope)
    }

    private fun loadAccountDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getAccountById(accountId)
                .onSuccess { accountResponse ->
                    val accountUiModel = accountResponse.toUiModel()
                    _state.update {
                        it.copy(
                            accountName = accountUiModel.name,
                            balance = accountUiModel.balance,
                            currency = accountUiModel.currency,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onError { networkError ->
                    _state.update { it.copy(isLoading = false, error = networkError.toUiText()) }
                }
        }
    }

    fun retry() {
        loadAccountDetails()
    }

    fun refresh() {
        refreshTrigger.value++
    }
}