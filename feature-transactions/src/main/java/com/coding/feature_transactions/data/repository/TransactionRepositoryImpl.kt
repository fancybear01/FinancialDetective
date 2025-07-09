package com.coding.feature_transactions.data.repository

import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.feature_transactions.data.remote.source.TransactionRemoteDataSource
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionRemoteDataSource
) : TransactionRepository {
    override suspend fun getTransactionsForPeriod(
        accountId: String,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>, NetworkError> {
        return remoteDataSource.getTransactionsForPeriod(accountId, startDate, endDate).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}