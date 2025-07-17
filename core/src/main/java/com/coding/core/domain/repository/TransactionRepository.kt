package com.coding.core.domain.repository

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
        id: Int, accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError>

    suspend fun deleteTransaction(id: Int): Result<Unit, NetworkError>

    suspend fun syncTransactionsForPeriod(
        accountId: String, startDate: String, endDate: String
    ): Result<Unit, NetworkError>

    suspend fun syncOfflineCreationsAndUpdates()

    suspend fun syncOfflineDeletions()

    suspend fun syncAllPending()
}
