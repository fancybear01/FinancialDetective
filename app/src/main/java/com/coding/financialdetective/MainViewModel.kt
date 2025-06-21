package com.coding.financialdetective

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteAccountDataSource
import com.coding.financialdetective.core.domain.repositories.AccountDataSource
import com.coding.financialdetective.core.domain.util.NetworkStatusTracker
import com.coding.financialdetective.core.domain.util.UiEvent
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.core.presentation.util.toUiText
import com.coding.financialdetective.models.domain_models.Account
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.networking.NetworkState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val networkStatusTracker = NetworkStatusTracker(application)

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Initial)

    val shouldShowNetworkErrorBanner: StateFlow<Boolean> =
        combine(_networkState, _currentAccount) { state, account ->
            state is NetworkState.Unavailable && account == null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    private val accountDataSource: AccountDataSource = RemoteAccountDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        observeNetwork()
        loadAccounts()
        _isReady.value = true
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkStatusTracker.networkState
                .distinctUntilChanged()
                .onEach { state ->
                    _networkState.value = state

                    if (state is NetworkState.Available && _currentAccount.value == null) {
                        loadAccounts()
                    }
                }
                .launchIn(this)
        }
    }

    fun loadAccounts(isForceRefresh: Boolean = false) {
        if (_currentAccount.value != null && !isForceRefresh) {
            return
        }

        viewModelScope.launch {
            accountDataSource
                .getAccounts()
                .onSuccess { accounts ->
                    _currentAccount.value = accounts.firstOrNull()
                }
                .onError { error ->
                    if (error != NetworkError.NO_INTERNET) {
                        _eventChannel.send(UiEvent.ShowSnackbar(error.toUiText()))
                    }
                }
        }
    }
}