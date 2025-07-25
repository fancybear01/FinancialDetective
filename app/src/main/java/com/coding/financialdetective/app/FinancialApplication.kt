package com.coding.financialdetective.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.coding.core.di.AppDependencies
import com.coding.core.util.SyncWorker
import com.coding.core_ui.di.AppDependenciesProvider
import com.coding.financialdetective.di.AppComponent
import com.coding.financialdetective.di.DaggerAppComponent
import com.coding.financialdetective.di.DaggerWorkerFactory
import java.util.concurrent.TimeUnit

class FinancialApplication : Application(), AppDependenciesProvider, Configuration.Provider  {

    internal lateinit var appComponent: AppComponent

    override val dependencies: AppDependencies
        get() = appComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        setupPeriodicSync()
    }

    override val workManagerConfiguration: Configuration
        get() {
            if (!::appComponent.isInitialized) {
                appComponent = DaggerAppComponent.factory().create(applicationContext)
            }

            return Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setWorkerFactory(DaggerWorkerFactory(
                    appComponent.transactionRepository(),
                    appComponent.preferencesManager()
                ))
                .build()
        }

    private fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 4,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodic_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )
    }
}