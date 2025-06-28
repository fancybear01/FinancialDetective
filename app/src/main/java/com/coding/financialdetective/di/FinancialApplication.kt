package com.coding.financialdetective.di

import android.app.Application
import com.coding.financialdetective.features.acccount.di.accountModule
import com.coding.financialdetective.features.categories.di.categoryModule
import com.coding.financialdetective.features.settings.di.settingsModule
import com.coding.financialdetective.features.transactions.di.transactionsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class FinancialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FinancialApplication)
            androidLogger()
            modules(
                appModule,
                networkModule,
                accountModule,
                categoryModule,
                transactionsModule,
                settingsModule
            )
        }
    }
}