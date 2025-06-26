package com.coding.financialdetective.features.acccount.di

import com.coding.financialdetective.features.acccount.data.remote.source.AccountRemoteDataSource
import com.coding.financialdetective.features.acccount.data.repository.AccountRepositoryImpl
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import com.coding.financialdetective.features.acccount.ui.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { (accountId: String) ->
        AccountViewModel(
            repository = get(),
            accountId = accountId
        )
    }

    single<AccountRepository> { AccountRepositoryImpl(get()) }
    factory { AccountRemoteDataSource(get()) }
}