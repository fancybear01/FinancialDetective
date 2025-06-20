package com.coding.financialdetective

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteAccountDataSource
import com.coding.financialdetective.core.domain.repositories.AccountDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.mappers.toUiModel
import com.coding.financialdetective.models.domain_models.Account
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val accountDataSource: AccountDataSource = RemoteAccountDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        loadAccounts()
        _isReady.value = true
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            accountDataSource
                .getAccounts()
                .onSuccess { accounts ->
                    if (_currentAccount.value == null) {
                        _currentAccount.value = accounts.firstOrNull()
                    }
                }
                .onError {  }
        }
    }
}