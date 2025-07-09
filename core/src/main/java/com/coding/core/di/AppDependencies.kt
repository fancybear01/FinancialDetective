package com.coding.core.di

import androidx.lifecycle.ViewModelProvider
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.repository.AccountRepository
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core.domain.repository.TransactionRepository
import io.ktor.client.HttpClient

interface AppDependencies {
    fun httpClient(): HttpClient
    fun connectivityObserver(): ConnectivityObserver
    fun accountRepository(): AccountRepository
    fun transactionRepository(): TransactionRepository
    fun categoryRepository(): CategoryRepository
    fun viewModelFactory(): ViewModelProvider.Factory
}