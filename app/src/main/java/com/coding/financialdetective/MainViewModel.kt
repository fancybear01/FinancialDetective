package com.coding.financialdetective

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

    private var initialLoadDone = false

    init {
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
                }
                .onError { error ->
                    _eventChannel.send(UiEvent.ShowSnackbar(error.toUiText()))
                }
            initialLoadDone = true
        }
    }
}