package com.coding.core_ui.navigation

import com.coding.core.domain.model.account_models.Account
import kotlinx.coroutines.flow.StateFlow

interface MainViewModelContract {
    val currentAccount: StateFlow<Account?>
    val accountUpdateTrigger: StateFlow<Int>

    fun onAccountManuallyUpdated(accountId: String, newName: String, newBalance: Double, newCurrencyCode: String)
    fun setTopBarAction(action: (() -> Unit)?)
    fun navigateBack()
}