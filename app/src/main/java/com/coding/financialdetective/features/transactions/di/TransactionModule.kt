package com.coding.financialdetective.features.transactions.di

import com.coding.financialdetective.features.transactions.data.remote.source.TransactionRemoteDataSource
import com.coding.financialdetective.features.transactions.data.repository.TransactionRepositoryImpl
import com.coding.financialdetective.features.transactions.domain.model.TransactionType
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository
import com.coding.financialdetective.features.transactions.ui.expenses_incomes.TransactionsViewModel
import com.coding.financialdetective.features.transactions.ui.my_history.MyHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionsModule = module {

    viewModel { (accountId: String, transactionType: TransactionType) ->
        TransactionsViewModel(
            repository = get(),
            accountId = accountId,
            transactionType = transactionType
        )
    }

    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    factory { TransactionRemoteDataSource(get()) }

    viewModel { (accountId: String) ->
        MyHistoryViewModel(
            repository = get(),
            accountId = accountId,
            savedStateHandle = get()
        )
    }
}