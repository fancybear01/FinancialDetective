package com.coding.financialdetective.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onError
import com.coding.core.util.UiEvent
import com.coding.core_ui.util.toUiText
import com.coding.core.domain.model.account_models.Account
import com.coding.core.domain.repository.AccountRepository
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core_ui.navigation.MainViewModelContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Основная view model приложения.
 */
class MainViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel(), MainViewModelContract {

    // Состояния для UI
    val isConnected = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    private val _currentAccount = MutableStateFlow<Account?>(null)
    override val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    // Каналы для одноразовых событий
    private val _eventChannel = Channel<UiEvent>()
    val events: Flow<UiEvent> = _eventChannel.receiveAsFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationEvents: Flow<NavigationEvent> = _navigationChannel.receiveAsFlow()


    // Управление TopBar
    var onTopBarActionClick by mutableStateOf<(() -> Unit)?>(null)
        private set

    private val _isTopBarActionEnabled = MutableStateFlow(false)
    override val isTopBarActionEnabled: StateFlow<Boolean> = _isTopBarActionEnabled.asStateFlow()

    override fun setTopBarAction(enabled: Boolean, action: (() -> Unit)?) {
        onTopBarActionClick = action
        _isTopBarActionEnabled.value = enabled && action != null
    }


    // Инициализация и логика
    init {
        observeAccounts()

        forceRefreshData()

        observeConnectivity()
    }

    private fun observeAccounts() {
        accountRepository.getAccountsStream()
            .onEach { accounts ->
                val firstAccount = accounts.firstOrNull()
                val current = _currentAccount.value
                if (current == null && firstAccount != null ||
                    current?.id != firstAccount?.id ||
                    (current != null && firstAccount != null && current != firstAccount)
                ) {
                    _currentAccount.value = firstAccount
                }
                _isReady.value = true
            }
            .launchIn(viewModelScope)
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            isConnected
                .drop(1)
                .filter { it }
                .debounce(1000)
                .collect {
                    forceRefreshData()
                }
        }
    }

    private fun forceRefreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            accountRepository.syncAccounts().onError { error ->
                _eventChannel.send(UiEvent.ShowSnackbar(error.toUiText())) }
            categoryRepository.syncCategories().onError { error ->
                _eventChannel.send(UiEvent.ShowSnackbar(error.toUiText()))
            }
            _isLoading.value = false
        }
    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
}