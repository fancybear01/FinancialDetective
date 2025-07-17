package com.coding.financialdetective.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.coding.core.data.sync.SyncWorker
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.preferences.PreferencesManager
import javax.inject.Inject
import javax.inject.Provider

class DaggerWorkerFactory @Inject constructor(
    private val transactionRepositoryProvider: Provider<TransactionRepository>,
    private val preferencesManagerProvider: Provider<PreferencesManager>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name ->
                SyncWorker(
                    appContext,
                    workerParameters,
                    transactionRepositoryProvider.get(),
                    preferencesManagerProvider.get()
                )
            else -> null
        }
    }
}