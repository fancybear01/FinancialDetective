package com.coding.financialdetective.features.acccount.data.mapper

import com.coding.financialdetective.features.acccount.data.remote.dto.AccountBriefDto
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountDto
import com.coding.financialdetective.features.acccount.data.remote.dto.AccountResponseDto
import com.coding.financialdetective.features.acccount.domain.model.Account
import com.coding.financialdetective.features.acccount.domain.model.AccountBrief
import com.coding.financialdetective.features.acccount.domain.model.AccountResponse


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