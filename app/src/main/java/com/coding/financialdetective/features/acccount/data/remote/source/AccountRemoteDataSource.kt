package com.coding.financialdetective.features.acccount.data.remote.source

import com.coding.financialdetective.data.remote.service.constructUrl
import com.coding.financialdetective.data.remote.service.safeCallWithRetry
import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountResponseDto

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
}