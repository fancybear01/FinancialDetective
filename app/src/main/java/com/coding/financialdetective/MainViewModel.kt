package com.coding.financialdetective

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.core_ui.util.UiEvent
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.features.acccount.domain.model.Account
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn

/**
 * Основная view model приложения.
 */
class MainViewModel(
    private val repository: AccountRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationEvents = _navigationChannel.receiveAsFlow()

    var onTopBarActionClick by mutableStateOf<(() -> Boolean)?>(null)
        private set

    fun setTopBarAction(action: (() -> Boolean)?) {
        onTopBarActionClick = action
    }

    private var initialLoadDone = false

    init {
        val instanceId = System.identityHashCode(this)
        loadAccounts()
        _isReady.value = true

        viewModelScope.launch {
            isConnected
                .drop(1)
                .debounce(1000)
                .collect { connected ->
                    if (connected) {
                        loadAccounts(true)
                    }
                }
        }
    }

    fun loadAccounts(isForceRefresh: Boolean = false) {
        if (currentAccount.value != null && !isForceRefresh && initialLoadDone) {
            return
        }

        viewModelScope.launch {
            repository
                .getAccounts()
                .onSuccess { accounts ->
                    _currentAccount.value = accounts.firstOrNull()
                    if (isForceRefresh) {
                        triggerAccountUpdate()
                    }
                }
                .onError { error ->
                    _eventChannel.send(UiEvent.ShowSnackbar(error.toUiText()))
                }
            initialLoadDone = true
        }
    }

    fun onAccountManuallyUpdated(
        accountId: String,
        newName: String,
        newBalance: Double,
        newCurrencyCode: String
    ) {
        val current = _currentAccount.value
        if (current?.id == accountId) {
            _currentAccount.value = current.copy(
                name = newName,
                balance = newBalance,
                currency = newCurrencyCode
            )
            triggerAccountUpdate()
        }
    }

    private val _accountUpdateTrigger = MutableStateFlow(0)
    val accountUpdateTrigger: StateFlow<Int> = _accountUpdateTrigger.asStateFlow()

    fun triggerAccountUpdate() {
        _accountUpdateTrigger.value++
    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
}