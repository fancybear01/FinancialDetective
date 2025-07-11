package com.coding.core.domain.repository

import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.domain.model.account_models.Account
import com.coding.core.domain.model.account_models.AccountResponse

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