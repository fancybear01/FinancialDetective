package com.coding.financialdetective.di

import android.content.Context
import com.coding.core.di.AppScope
import com.coding.financialdetective.data.remote.connectivity.AndroidConnectivityObserver
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides

@Module
object AppModule {
    @Provides
    @AppScope
    fun provideConnectivityObserver(context: Context): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }
}