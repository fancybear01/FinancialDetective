package com.coding.financialdetective.features.transactions.data.mapper

import com.coding.financialdetective.features.acccount.data.mapper.toDomain
import com.coding.financialdetective.features.categories.data.mapper.toDomain
import com.coding.financialdetective.features.transactions.data.remote.dto.TransactionResponseDto
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import java.time.ZonedDateTime

fun TransactionResponseDto.toDomain(): Transaction {
    return Transaction(
        id = this.id,
        account = this.account.toDomain(),
        category = this.category.toDomain(),
        amount = this.amount.toDouble(),
        transactionDate = ZonedDateTime.parse(this.transactionDate),
        comment = this.comment,
        createdAt = ZonedDateTime.parse(this.createdAt),
        updatedAt = ZonedDateTime.parse(this.updatedAt)
    )
}