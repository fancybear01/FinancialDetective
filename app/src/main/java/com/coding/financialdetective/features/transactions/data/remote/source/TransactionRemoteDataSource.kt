package com.coding.financialdetective.features.transactions.data.remote.source

import com.coding.financialdetective.data.remote.service.constructUrl
import com.coding.financialdetective.data.remote.service.safeCallWithRetry
import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.transactions.data.remote.dto.TransactionResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TransactionRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getTransactionsForPeriod(
        accountId: String,
        startDate: String,
        endDate: String
    ): Result<List<TransactionResponseDto>, NetworkError> {
        return safeCallWithRetry<List<TransactionResponseDto>> {
            httpClient.get(constructUrl("transactions/account/$accountId/period")) {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }
        }
    }
}