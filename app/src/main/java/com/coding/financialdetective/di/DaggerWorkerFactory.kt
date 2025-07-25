package com.coding.financialdetective.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.coding.core.util.SyncWorker
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.preferences.PreferencesManager

class DaggerWorkerFactory(
    private val transactionRepository: TransactionRepository,
    private val preferencesManager: PreferencesManager
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
                    transactionRepository,
                    preferencesManager
                )
            else -> {
                null
            }
        }
    }
}