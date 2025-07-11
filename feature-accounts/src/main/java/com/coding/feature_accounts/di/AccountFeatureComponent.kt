package com.coding.feature_accounts.di

import com.coding.core.di.FeatureScope
import com.coding.core.di.AppDependencies
import com.coding.feature_accounts.ui.account_info.AccountViewModel
import com.coding.feature_accounts.ui.editing_an_account.EditAccountViewModel
import dagger.Component

@FeatureScope
@Component(
    dependencies = [AppDependencies::class]
)
interface AccountFeatureComponent {

    fun accountViewModelFactory(): AccountViewModel.Factory
    fun editAccountViewModelFactory(): EditAccountViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(appDependencies: AppDependencies): AccountFeatureComponent
    }
}