package com.coding.feature_transactions.data.repository

import com.coding.core.data.dto.transactions_dto.TransactionRequestDto
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.feature_transactions.data.remote.source.TransactionRemoteDataSource
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.TransactionRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
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

    override suspend fun getTransactionById(id: Int): Result<Transaction, NetworkError> {
        return remoteDataSource.getTransactionById(id).map { it.toDomain() }
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        transactionDate: ZonedDateTime,
        amount: Double,
        comment: String
    ): Result<Unit, NetworkError> {
        val request = TransactionRequestDto(
            accountId = accountId,
            categoryId = categoryId,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            amount = "%.2f".format(Locale.US, amount),
            comment = comment
        )
        return remoteDataSource.createTransaction(request)
    }

    override suspend fun updateTransaction(
        id: Int,
        accountId: Int,
        categoryId: Int,
        transactionDate: ZonedDateTime,
        amount: Double,
        comment: String
    ): Result<Unit, NetworkError> {
        val request = TransactionRequestDto(
            accountId = accountId,
            categoryId = categoryId,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            amount = "%.2f".format(Locale.US, amount),
            comment = comment
        )
        return remoteDataSource.updateTransaction(id, request)
    }

    override suspend fun deleteTransaction(id: Int): Result<Unit, NetworkError> {
        return remoteDataSource.deleteTransaction(id)
    }
}