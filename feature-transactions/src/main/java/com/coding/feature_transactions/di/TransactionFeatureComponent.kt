package com.coding.feature_transactions.di

import com.coding.core.di.AppDependencies
import com.coding.core.di.FeatureScope
import com.coding.feature_transactions.ui.expenses_incomes.TransactionsViewModel
import com.coding.feature_transactions.ui.my_history.MyHistoryViewModel
import dagger.Component

@FeatureScope
@Component(
    dependencies = [AppDependencies::class]
)
interface TransactionFeatureComponent {

    fun transactionsViewModelFactory(): TransactionsViewModel.Factory
    fun myHistoryViewModelFactory(): MyHistoryViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(appDependencies: AppDependencies): TransactionFeatureComponent
    }
}