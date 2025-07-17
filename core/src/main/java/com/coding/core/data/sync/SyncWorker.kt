package com.coding.core.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.preferences.PreferencesManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val transactionRepository: TransactionRepository,
    private val preferencesManager: PreferencesManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            transactionRepository.syncAllPending()
            preferencesManager.lastSyncTimestamp = System.currentTimeMillis()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}