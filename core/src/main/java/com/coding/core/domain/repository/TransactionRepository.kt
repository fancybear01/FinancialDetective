package com.coding.core.domain.repository

import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.domain.model.transactions_models.Transaction

/**
 * Репозиторий для работы с транзакциями (доходами/расходами).
 */
interface TransactionRepository {
    suspend fun getTransactionsForPeriod(
        accountId: String,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>, NetworkError>
}