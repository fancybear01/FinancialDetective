package com.coding.feature_accounts.data.local.source

import com.coding.core.data.local.dao.AccountDao
import com.coding.core.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountLocalDataSource @Inject constructor(
    private val accountDao: AccountDao
) {
    fun getAccountsStream(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    suspend fun upsertAccounts(accounts: List<AccountEntity>) {
        accountDao.upsertAll(accounts)
    }
}