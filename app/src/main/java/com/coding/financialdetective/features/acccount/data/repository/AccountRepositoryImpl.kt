package com.coding.financialdetective.features.acccount.data.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.features.acccount.data.remote.source.AccountRemoteDataSource
import com.coding.financialdetective.features.acccount.domain.model.Account
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.data.util.map
import com.coding.financialdetective.features.acccount.data.mapper.toDomain
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse
import com.coding.financialdetective.features.acccount.domain.repository.AccountRepository
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