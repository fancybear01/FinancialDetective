package com.coding.financialdetective.core.data.remote

import com.coding.financialdetective.core.domain.repositories.AccountDataSource
import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.core.domain.util.map
import com.coding.financialdetective.core.networking.constructUrl
import com.coding.financialdetective.core.networking.safeCall
import com.coding.financialdetective.core.networking.safeCallWithRetry
import com.coding.financialdetective.mappers.toDomain
import com.coding.financialdetective.models.data_models.AccountDto
import com.coding.financialdetective.models.data_models.AccountResponseDto
import com.coding.financialdetective.models.domain_models.Account
import com.coding.financialdetective.models.domain_models.AccountResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteAccountDataSource(
    private val httpClient: HttpClient
) : AccountDataSource {
    override suspend fun getAccounts(): Result<List<Account>, NetworkError> {
        return safeCallWithRetry<List<AccountDto>> {
            httpClient.get(constructUrl("accounts"))
        }.map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(id: String): Result<AccountResponse, NetworkError> {
        return safeCallWithRetry<AccountResponseDto> {
            httpClient.get(constructUrl("accounts/$id"))
        }.map {
            it.toDomain()
        }
    }
}