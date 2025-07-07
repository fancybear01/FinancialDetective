package com.coding.financialdetective.di

import com.coding.financialdetective.features.acccount.data.repository.AccountRepositoryImpl
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
import com.coding.financialdetective.features.categories.data.repository.CategoryRepositoryImpl
import com.coding.financialdetective.features.categories.domain.repository.CategoryRepository
import com.coding.financialdetective.features.transactions.data.repository.TransactionRepositoryImpl
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    @AppScope
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @AppScope
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @AppScope
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}