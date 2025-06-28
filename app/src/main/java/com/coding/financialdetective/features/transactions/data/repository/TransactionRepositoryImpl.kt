package com.coding.financialdetective.features.transactions.data.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.data.util.map
import com.coding.financialdetective.features.transactions.data.mapper.toDomain
import com.coding.financialdetective.features.transactions.data.remote.source.TransactionRemoteDataSource
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
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