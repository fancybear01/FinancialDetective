package com.coding.financialdetective.mappers

import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.models.data_models.AccountBriefDto
import com.coding.financialdetective.models.data_models.AccountDto
import com.coding.financialdetective.models.data_models.AccountResponseDto
import com.coding.financialdetective.models.data_models.StatItemDto
import com.coding.financialdetective.models.domain_models.Account
import com.coding.financialdetective.models.domain_models.AccountBrief
import com.coding.financialdetective.models.domain_models.AccountResponse
import com.coding.financialdetective.models.ui_models.AccountUi

fun AccountBriefDto.toDomain(): AccountBrief {
    return AccountBrief(
        id = this.id,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
    )
}

fun AccountDto.toDomain(): Account {
    return Account(
        id = this.id,
        userId = this.userId,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

//fun Account.toUiModel(): AccountUi {
//    return AccountUi(
//        id = this.id,
//        name = this.name,
//        balance = formatNumberWithSpaces(this.balance),
//        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
//        createdAt = this.createdAt,
//        updatedAt = this.updatedAt
//    )
//}

fun AccountResponseDto.toDomain(): AccountResponse {
    return AccountResponse(
        id = this.id,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
        incomeStats = this.incomeStats.map { incomeStat -> incomeStat.toDomain() },
        expenseStats = this.expenseStats.map { expenseStat -> expenseStat.toDomain() },
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun AccountResponse.toUiModel(): AccountUi {
    return AccountUi(
        id = this.id,
        name = this.name,
        balance = formatNumberWithSpaces(this.balance),
        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}