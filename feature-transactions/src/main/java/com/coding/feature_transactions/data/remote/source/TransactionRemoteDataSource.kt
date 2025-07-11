package com.coding.feature_transactions.data.remote.source

import com.coding.core.data.dto.transactions_dto.TransactionRequestDto
import com.coding.core.data.remote.service.constructUrl
import com.coding.core.data.remote.service.safeCallWithRetry
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.dto.transactions_dto.TransactionResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    suspend fun getTransactionById(id: Int): Result<TransactionResponseDto, NetworkError> {
        return safeCallWithRetry {
            httpClient.get(constructUrl("transactions/$id"))
        }
    }

    suspend fun createTransaction(request: TransactionRequestDto): Result<Unit, NetworkError> {
        return safeCallWithRetry {
            httpClient.post(constructUrl("transactions")) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun updateTransaction(id: Int, request: TransactionRequestDto): Result<Unit, NetworkError> {
        return safeCallWithRetry {
            httpClient.put(constructUrl("transactions/$id")) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun deleteTransaction(id: Int): Result<Unit, NetworkError> {
        return safeCallWithRetry {
            httpClient.delete(constructUrl("transactions/$id"))
        }
    }
}