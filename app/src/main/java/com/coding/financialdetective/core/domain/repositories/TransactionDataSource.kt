package com.coding.financialdetective.core.domain.repositories

import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.models.domain_models.Transaction

interface TransactionDataSource {
    suspend fun getTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>, NetworkError>
}