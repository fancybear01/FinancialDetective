package com.coding.feature_accounts.data.repository

import com.coding.core.data.util.NetworkError
import com.coding.feature_accounts.data.remote.source.AccountRemoteDataSource
import com.coding.core.domain.model.account_models.Account
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.core.domain.model.account_models.AccountResponse
import com.coding.core.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource
) : AccountRepository {

    override suspend fun getAccounts(): Result<List<Account>, NetworkError> {
        return remoteDataSource.getAccounts().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(id: String): Result<AccountResponse, NetworkError> {
        return remoteDataSource.getAccountById(id).map { it.toDomain() }
    }

    override suspend fun updateAccount(
        accountId: String,
        name: String,
        balance: Double,
        currency: String
    ): Result<Unit, NetworkError> {
        return remoteDataSource
            .updateAccount(
                accountId = accountId,
                name = name,
                balance = balance,
                currency = currency
            )
    }
}