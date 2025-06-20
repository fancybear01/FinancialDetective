package com.coding.financialdetective.core.data.remote

import com.coding.financialdetective.core.domain.repositories.TransactionDataSource
import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.core.domain.util.map
import com.coding.financialdetective.core.networking.constructUrl
import com.coding.financialdetective.core.networking.safeCall
import com.coding.financialdetective.mappers.toDomain
import com.coding.financialdetective.models.data_models.TransactionResponseDto
import com.coding.financialdetective.models.domain_models.Transaction
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteTransactionDataSource(
    private val httpClient: HttpClient
): TransactionDataSource {
    override suspend fun getTransactionsForPeriod(
        accountId: String,
        startDate: String,
        endDate: String
    ): Result<List<Transaction>, NetworkError> {
        return safeCall<List<TransactionResponseDto>> {
            httpClient.get(constructUrl("transactions/account/$accountId/period")) {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }
        }.map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}