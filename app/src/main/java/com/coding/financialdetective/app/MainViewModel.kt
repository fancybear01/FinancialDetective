package com.coding.financialdetective.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onSuccess
import com.coding.core.data.util.onError
import com.coding.core.util.UiEvent
import com.coding.core_ui.util.toUiText
import com.coding.core.domain.model.account_models.Account
import com.coding.core.domain.repository.AccountRepository
import com.coding.core_ui.navigation.MainViewModelContract
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
import javax.inject.Inject

/**
 * Основная view model приложения.
 */
class MainViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel(), MainViewModelContract {

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    private val _currentAccount = MutableStateFlow<Account?>(null)
    override val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationEvents = _navigationChannel.receiveAsFlow()

    var onTopBarActionClick by mutableStateOf<(() -> Unit)?>(null)
        private set

    private val _isTopBarActionEnabled = MutableStateFlow(false)
    override val isTopBarActionEnabled: StateFlow<Boolean> = _isTopBarActionEnabled.asStateFlow()

    override fun setTopBarAction(enabled: Boolean, action: (() -> Unit)?) {
        onTopBarActionClick = action
        _isTopBarActionEnabled.value = enabled && action != null
    }

    override fun navigateBack() {
        viewModelScope.launch {
            _navigationChannel.send(NavigationEvent.NavigateBack)
        }
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

    override fun onAccountManuallyUpdated(
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
    override val accountUpdateTrigger: StateFlow<Int> = _accountUpdateTrigger.asStateFlow()

    fun triggerAccountUpdate() {
        _accountUpdateTrigger.value++
    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
}