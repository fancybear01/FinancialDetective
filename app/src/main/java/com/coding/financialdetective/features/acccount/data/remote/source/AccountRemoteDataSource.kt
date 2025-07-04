package com.coding.financialdetective.features.acccount.data.remote.source

import com.coding.financialdetective.data.remote.service.constructUrl
import com.coding.financialdetective.data.remote.service.safeCallWithRetry
import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountResponseDto
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.util.Locale

class AccountRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getAccounts(): Result<List<AccountDto>, NetworkError> {
        return safeCallWithRetry<List<AccountDto>> {
            httpClient.get(constructUrl("accounts"))
        }
    }

    suspend fun getAccountById(id: String): Result<AccountResponseDto, NetworkError> {
        return safeCallWithRetry<AccountResponseDto> {
            httpClient.get(constructUrl("accounts/$id"))
        }
    }

    suspend fun updateAccount(
        accountId: String,
        name: String,
        balance: Double,
        currency: String
    ): Result<Unit, NetworkError> {
        return safeCallWithRetry<Unit> {
            httpClient.put(constructUrl("accounts/$accountId")) {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateAccountRequest(
                        name = name,
                        balance = "%.2f".format(Locale.US, balance),
                        currency = currency
                    )
                )
            }
        }
    }
}
@Serializable
data class UpdateAccountRequest(
    val name: String,
    val balance: String,
    val currency: String
)