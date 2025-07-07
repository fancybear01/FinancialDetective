package com.coding.financialdetective.di

import com.coding.financialdetective.features.acccount.ui.account_info.AccountViewModel
import com.coding.financialdetective.features.acccount.ui.editing_an_account.EditAccountViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@AccountScope
@Subcomponent
interface AccountFeatureComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @AccountId accountId: String): AccountFeatureComponent
    }

    fun accountViewModelFactory(): AccountViewModel.Factory
    fun editAccountViewModelFactory(): EditAccountViewModel.Factory
}