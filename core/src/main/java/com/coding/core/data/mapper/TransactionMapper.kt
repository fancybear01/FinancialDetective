package com.coding.core.data.mapper

import com.coding.core.data.dto.transactions_dto.TransactionResponseDto
import com.coding.core.domain.model.transactions_models.Transaction
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