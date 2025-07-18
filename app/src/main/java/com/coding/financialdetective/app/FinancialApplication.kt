package com.coding.financialdetective.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.coding.core.util.SyncWorker
import com.coding.core_ui.di.AppDependenciesProvider
import com.coding.financialdetective.di.AppComponent
import com.coding.financialdetective.di.DaggerAppComponent
import com.coding.financialdetective.di.DaggerWorkerFactory
import java.util.concurrent.TimeUnit

class FinancialApplication : Application(), AppDependenciesProvider, Configuration.Provider {
    override val dependencies: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override val workManagerConfiguration: Configuration
        get() {
            val factory = DaggerWorkerFactory(
                dependencies.transactionRepository(),
                dependencies.preferencesManager()
            )
            return Configuration.Builder()
                .setWorkerFactory(factory)
                .build()
        }

    override fun onCreate() {
        super.onCreate()
        setupPeriodicSync()
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