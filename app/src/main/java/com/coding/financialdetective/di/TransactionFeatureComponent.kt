package com.coding.financialdetective.di

import com.coding.financialdetective.features.transactions.ui.expenses_incomes.TransactionsViewModel
import com.coding.financialdetective.features.transactions.ui.my_history.MyHistoryViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@AccountScope
@Subcomponent
interface TransactionFeatureComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @AccountId accountId: String): TransactionFeatureComponent
    }

    fun transactionsViewModelFactory(): TransactionsViewModel.Factory
    fun myHistoryViewModelFactory(): MyHistoryViewModel.Factory
}