package com.coding.financialdetective.features.transactions.domain.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.transactions.domain.model.Transaction

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