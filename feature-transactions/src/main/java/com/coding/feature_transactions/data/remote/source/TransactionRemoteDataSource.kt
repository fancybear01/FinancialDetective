package com.coding.feature_transactions.data.remote.source

import com.coding.core.data.remote.service.constructUrl
import com.coding.core.data.remote.service.safeCallWithRetry
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.dto.transactions_dto.TransactionResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class TransactionRemoteDataSource @Inject constructor(
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