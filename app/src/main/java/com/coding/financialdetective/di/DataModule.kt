package com.coding.financialdetective.di

import com.coding.core.di.AppScope
import com.coding.feature_accounts.data.repository.AccountRepositoryImpl
import com.coding.core.domain.repository.AccountRepository
import com.coding.feature_categories.data.repository.CategoryRepositoryImpl
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core.domain.repository.SecurityRepository
import com.coding.feature_transactions.data.repository.TransactionRepositoryImpl
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.domain.repository.UserPreferencesRepository
import com.coding.financialdetective.util.SecurityRepositoryImpl
import com.coding.financialdetective.util.UserPreferencesRepositoryImpl
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

    @Binds
    @AppScope
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @AppScope
    abstract fun bindSecurityRepository(impl: SecurityRepositoryImpl): SecurityRepository
}