package com.coding.financialdetective.features.acccount.domain.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountResponseDto
import com.coding.financialdetective.features.acccount.domain.model.Account
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse

/**
 * Репозиторий для работы с счетами.
 */
interface AccountRepository {
    suspend fun getAccounts(): Result<List<Account>, NetworkError>
    suspend fun getAccountById(id: String): Result<AccountResponse, NetworkError>
    suspend fun updateAccount(
        accountId: String,
        name: String,
        balance: Double,
        currency: String
    ): Result<Unit, NetworkError>
}