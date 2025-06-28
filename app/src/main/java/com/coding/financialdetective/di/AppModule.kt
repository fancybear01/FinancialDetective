package com.coding.financialdetective.di

import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.data.remote.connectivity.AndroidConnectivityObserver
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ConnectivityObserver> { AndroidConnectivityObserver(androidContext()) }

    viewModel {
        MainViewModel(
            repository = get(),
            connectivityObserver = get()
        )
    }
}