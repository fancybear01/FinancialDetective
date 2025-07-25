package com.coding.feature_accounts.ui.editing_an_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core.domain.model.account_models.AccountResponse
import com.coding.core.domain.model.account_models.Currency
import com.coding.core.domain.repository.AccountRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана редактирования аккаунта.
 * Управляет состоянием редактирования, загрузкой данных аккаунта и их сохранением.
 *
 * @param repository Репозиторий для доступа к данным счетов
 * @param accountId Уникальный идентификатор редактируемого счёта
 * @param connectivityObserver Наблюдатель за состоянием сетевого подключения
 */
class EditAccountViewModel @AssistedInject constructor(
    private val repository: AccountRepository,
    @Assisted private val accountId: String,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String): EditAccountViewModel
    }

    private val _state = MutableStateFlow(EditAccountState())
    val state: StateFlow<EditAccountState> = _state.asStateFlow()

    private val _saveSuccessEvent = Channel<Unit>()
    val saveSuccessEvent = _saveSuccessEvent.receiveAsFlow()

    private var originalAccount: AccountResponse? = null

    init {
        loadAccount()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .drop(1).debounce(1000)
            .filter { it && state.value.error != null }
            .onEach { retry() }
            .launchIn(viewModelScope)
    }

    private fun loadAccountForEditing() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getAccountById(accountId)
                .onSuccess { accountResponse ->
                    originalAccount = accountResponse
                    _state.update {
                        it.copy(
                            isLoading = false,
                            accountName = accountResponse.name,
                            balance = formatNumberWithSpaces(accountResponse.balance),
                            rawBalance = accountResponse.balance,
                            selectedCurrency = Currency.fromCode(accountResponse.currency),
                            currencySymbol = Currency.fromCode(accountResponse.currency).symbol,
                            hasChanges = false
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getAccountById(accountId)
                .onSuccess { account ->
                    originalAccount = account
                    _state.update {
                        it.copy(
                            isLoading = false,
                            accountName = account.name,
                            balance = formatNumberWithSpaces(account.balance),
                            rawBalance = account.balance,
                            selectedCurrency = Currency.fromCode(account.currency),
                            currencySymbol = Currency.fromCode(account.currency).symbol,
                            hasChanges = false
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    fun onBalanceChanged(newBalanceStr: String) {
        val cleanedBalance = newBalanceStr.replace("\\s".toRegex(), "").replace(",", ".")
        val newRawBalance = cleanedBalance.toDoubleOrNull() ?: state.value.rawBalance

        _state.update {
            it.copy(
                balance = formatNumberWithSpaces(newRawBalance),
                rawBalance = newRawBalance,
                hasChanges = checkForChanges(name = it.accountName, rawBalance = newRawBalance, currency = it.selectedCurrency)
            )
        }
    }

    fun onAccountNameChanged(name: String) {
        _state.update {
            it.copy(
                accountName = name,
                hasChanges = checkForChanges(name = name, rawBalance = it.rawBalance, currency = it.selectedCurrency)
            )
        }
    }

    fun onCurrencySelected(currency: Currency) {
        _state.update {
            it.copy(
                selectedCurrency = currency,
                currencySymbol = currency.symbol,
                hasChanges = checkForChanges(name = it.accountName, rawBalance = it.rawBalance, currency = currency)
            )
        }
    }

    private fun checkForChanges(name: String, rawBalance: Double, currency: Currency): Boolean {
        val original = originalAccount ?: return false

        val nameChanged = name != original.name
        val balanceChanged = rawBalance != original.balance
        val currencyChanged = currency.code != original.currency

        val result = nameChanged || balanceChanged || currencyChanged

        return result
    }

    fun saveChanges() {
        if (!state.value.hasChanges) return
        val original = originalAccount ?: return

        val currentState = state.value
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            repository.updateAccount(
                accountId = accountId,
                name = currentState.accountName,
                balance = currentState.rawBalance,
                currency = currentState.selectedCurrency.code
            ).onSuccess {
                _state.update { it.copy(isLoading = false, hasChanges = false) }
                _saveSuccessEvent.send(Unit)
            }.onError { error ->
                _state.update { it.copy(isLoading = false, error = error.toUiText()) }
            }
        }
    }

    fun retry() {
        loadAccountForEditing()
    }
}