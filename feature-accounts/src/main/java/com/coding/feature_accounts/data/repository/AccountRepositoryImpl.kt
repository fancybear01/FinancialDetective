package com.coding.feature_accounts.data.repository

import com.coding.core.data.util.NetworkError
import com.coding.feature_accounts.data.remote.source.AccountRemoteDataSource
import com.coding.core.domain.model.account_models.Account
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.core.data.mapper.toEntity
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onSuccess
import com.coding.core.domain.model.account_models.AccountResponse
import com.coding.core.domain.repository.AccountRepository
import com.coding.feature_accounts.data.local.source.AccountLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val localDataSource: AccountLocalDataSource,
    private val connectivityObserver: ConnectivityObserver
) : AccountRepository {

    override fun getAccountsStream(): Flow<List<Account>> {
        return localDataSource.getAccountsStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncAccounts(): Result<Unit, NetworkError> {
        if (!connectivityObserver.isConnected.first()) {
            return Result.Success(Unit)
        }

        return remoteDataSource.getAccounts().onSuccess { dtoList ->
            val entities = dtoList.map { it.toEntity() }
            localDataSource.upsertAccounts(entities)
        }.map { }
    }

    override suspend fun getAccountById(id: String): Result<AccountResponse, NetworkError> {
        return remoteDataSource.getAccountById(id).map { it.toDomain() }
    }

    override suspend fun updateAccount(
        accountId: String, name: String, balance: Double, currency: String
    ): Result<Unit, NetworkError> {
        val updateResult = remoteDataSource.updateAccount(
            accountId = accountId,
            name = name,
            balance = balance,
            currency = currency
        )

        if (updateResult is Result.Success) {
            syncAccounts()
        }

        return updateResult
    }
}