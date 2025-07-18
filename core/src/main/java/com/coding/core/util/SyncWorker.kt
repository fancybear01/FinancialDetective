package com.coding.core.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.preferences.PreferencesManager

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
            if (runAttemptCount < 5) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}