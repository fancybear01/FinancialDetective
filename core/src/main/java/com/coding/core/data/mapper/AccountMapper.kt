package com.coding.core.data.mapper

import com.coding.core.data.local.entity.AccountEntity
import com.coding.core.data.remote.dto.account_dto.AccountBriefDto
import com.coding.core.data.remote.dto.account_dto.AccountDto
import com.coding.core.data.remote.dto.account_dto.AccountResponseDto
import com.coding.core.domain.model.account_models.Account
import com.coding.core.domain.model.account_models.AccountBrief
import com.coding.core.domain.model.account_models.AccountResponse


fun AccountBriefDto.toDomain(): AccountBrief {
    return AccountBrief(
        id = this.id.toString(),
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency,
    )
}

fun AccountDto.toDomain(): Account {
    return Account(
        id = this.id.toString(),
        userId = this.userId,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun AccountResponseDto.toDomain(): AccountResponse {
    return AccountResponse(
        id = this.id.toString(),
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency,
        incomeStats = this.incomeStats.map { incomeStat -> incomeStat.toDomain() },
        expenseStats = this.expenseStats.map { expenseStat -> expenseStat.toDomain() },
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun AccountEntity.toDomainBrief(): AccountBrief {
    return AccountBrief(
        id = this.accountId,
        name = this.name,
        balance = this.balance,
        currency = this.currency
    )
}

fun AccountDto.toEntity(): AccountEntity {
    return AccountEntity(
        accountId = this.id.toString(),
        userId = this.userId,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun AccountEntity.toDomain(): Account {
    return Account(
        id = this.accountId,
        userId = this.userId,
        name = this.name,
        balance = this.balance,
        currency = this.currency,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}