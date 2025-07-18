package com.coding.core.domain.repository

import androidx.work.WorkInfo
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.domain.model.transactions_models.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

/**
 * Репозиторий для работы с транзакциями (доходами/расходами).
 */
interface TransactionRepository {

    fun getTransactionsStream(accountId: String, startDate: String, endDate: String): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Int): Result<Transaction, NetworkError>

    suspend fun createTransaction(
        accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError>

    suspend fun updateTransaction(
        id: String, accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError>

    suspend fun deleteTransaction(id: String): Result<Unit, NetworkError>

    suspend fun syncTransactionsForPeriod(
        accountId: String, startDate: String, endDate: String
    ): Result<Unit, NetworkError>

    suspend fun syncOfflineCreationsAndUpdates()

    suspend fun syncOfflineDeletions()

    suspend fun syncAllPending()

    suspend fun syncAllData(accountId: String): Result<Unit, NetworkError>

    fun getSyncWorkInfo(): Flow<WorkInfo?>

    suspend fun getLocalTransactionById(localId: Long): Result<Transaction, NetworkError>
}
