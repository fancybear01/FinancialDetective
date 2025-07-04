package com.coding.financialdetective.features.acccount.di

import com.coding.financialdetective.features.acccount.data.remote.source.AccountRemoteDataSource
import com.coding.financialdetective.features.acccount.data.repository.AccountRepositoryImpl
import com.coding.financialdetective.features.acccount.domain.model.Currency
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import com.coding.financialdetective.features.acccount.ui.account_info.AccountViewModel
import com.coding.financialdetective.features.acccount.ui.editing_an_account.EditAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { (accountId: String) ->
        AccountViewModel(
            repository = get(),
            accountId = accountId,
            connectivityObserver = get()
        )
    }

    viewModel { (accountId: String) ->
        EditAccountViewModel(
            repository = get(),
            accountId = accountId,
            connectivityObserver = get()
        )
    }

    single<AccountRepository> { AccountRepositoryImpl(get()) }
    factory { AccountRemoteDataSource(get()) }
    factory { (currencyCode: String) ->
        Currency.fromCode(currencyCode)
    }
}