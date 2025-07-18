package com.coding.core_ui.navigation

import com.coding.core.domain.model.account_models.Account
import kotlinx.coroutines.flow.StateFlow

interface MainViewModelContract {
    val currentAccount: StateFlow<Account?>
    val isTopBarActionEnabled: StateFlow<Boolean>
    fun setTopBarAction(enabled: Boolean = true, action: (() -> Unit)?)
}