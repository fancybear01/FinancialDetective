package com.coding.core.util

import android.content.Context
import android.util.Log
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
        Log.i("SyncWorker", "--- SyncWorker STARTED ---")
        return try {
            transactionRepository.syncAllPending()
            preferencesManager.lastSyncTimestamp = System.currentTimeMillis()
            Log.i("SyncWorker", "--- SyncWorker SUCCEEDED ---")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "--- SyncWorker FAILED, will retry ---", e)
            Result.retry()
        }
    }
}