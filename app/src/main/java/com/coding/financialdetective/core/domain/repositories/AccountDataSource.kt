package com.coding.financialdetective.core.domain.repositories

import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.models.domain_models.Account
import com.coding.financialdetective.models.domain_models.AccountResponse

interface AccountDataSource {
    suspend fun getAccounts(): Result<List<Account>, NetworkError>
    suspend fun getAccountById(id: String): Result<AccountResponse, NetworkError>
}