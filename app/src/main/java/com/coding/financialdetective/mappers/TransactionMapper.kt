package com.coding.financialdetective.mappers

import java.time.format.DateTimeFormatter
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.models.data_models.TransactionResponseDto
import com.coding.financialdetective.models.domain_models.Transaction
import com.coding.financialdetective.models.ui_models.TransactionUi
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

fun Transaction.toUiModel(): TransactionUi {
    return TransactionUi(
        id = this.id,
        categoryName = this.category.name,
        categoryEmoji = this.category.emoji,
        comment = this.comment,
        formattedAmount = formatNumberWithSpaces(this.amount) + " " + this.account.currency,
        formattedDate = this.transactionDate.format(DateTimeFormatter.ofPattern("HH:mm"))
    )
}