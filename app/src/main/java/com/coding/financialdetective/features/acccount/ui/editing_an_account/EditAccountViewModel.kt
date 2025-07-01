package com.coding.financialdetective.features.acccount.ui.editing_an_account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core_ui.util.UiText
import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAccountViewModel(
    private val repository: AccountRepository,
    private val accountId: String,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(EditAccountState())
    val state: StateFlow<EditAccountState> = _state.asStateFlow()

    private var originalAccount: AccountResponse? = null

    init {
        loadAccount()
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

    fun retry() {
        loadAccount()
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
        Log.d("VIEW_MODEL_DEBUG", "onAccountNameChanged called with: '$name'")
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

        Log.d("VIEW_MODEL_DEBUG", """
        --- Checking for changes ---
        Original Name: '${original.name}', New Name: '$name', Changed: $nameChanged
        Original Balance: ${original.balance}, New Balance: $rawBalance, Changed: $balanceChanged
        Original Currency: '${original.currency}', New Currency: '${currency.code}', Changed: $currencyChanged
        ----------------------------
        Total hasChanges: $result
    """.trimIndent())

        return result
    }

    fun saveChanges() {
        Log.d("UPDATE_ACCOUNT_TAG", "ViewModel: saveChanges() called. Has changes: ${state.value.hasChanges}")
        if (!state.value.hasChanges) return

        viewModelScope.launch {
            val currentState = state.value
            _state.update { it.copy(isLoading = true) }

            repository.updateAccount(
                accountId = accountId,
                name = currentState.accountName,
                balance = currentState.rawBalance,
                currency = currentState.selectedCurrency.code
            ).onSuccess {
                _state.update { it.copy(isSaved = true, isLoading = false) }
            }.onError { error ->
                _state.update { it.copy(error = error.toUiText(), isLoading = false) }
            }
        }
    }

    fun showDiscardDialog() {
        _state.update { it.copy(showDiscardDialog = true) }
    }

    fun hideDiscardDialog() {
        _state.update { it.copy(showDiscardDialog = false) }
    }

    fun discardChanges() {
        originalAccount?.let { original ->
            _state.update {
                it.copy(
                    accountName = original.name,
                    selectedCurrency = Currency.fromCode(original.currency),
                    currencySymbol = Currency.fromCode(original.currency).symbol,
                    hasChanges = false,
                )
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

data class EditAccountState(
    val accountName: String = "",
    val balance: String = "0",
    val rawBalance: Double = 0.0,
    val selectedCurrency: Currency = Currency.RUB,
    val currencySymbol: String = "₽",
    val isSaveEnabled: Boolean = accountName.isNotBlank(),
    val hasChanges: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

enum class Currency(
    val code: String,
    val displayName: String,
    val symbol: String
) {
    RUB("RUB", "Российский рубль", "₽"),
    USD("USD", "Американский доллар", "$"),
    EUR("EUR", "Евро", "€");

    companion object {
        fun fromCode(code: String): Currency {
            return values().find { it.code == code } ?: RUB
        }
    }
}