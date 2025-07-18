package com.coding.core.data.mapper

import android.util.Log
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import com.coding.core.data.remote.dto.transactions_dto.TransactionRequestDto
import com.coding.core.data.remote.dto.transactions_dto.TransactionResponseDto
import com.coding.core.domain.model.transactions_models.Transaction
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun TransactionResponseDto.toDomain(): Transaction {
    return Transaction(
        id = this.id.toString(),
        account = this.account.toDomain(),
        category = this.category.toDomain(),
        amount = this.amount.toDouble(),
        transactionDate = ZonedDateTime.parse(this.transactionDate),
        comment = this.comment,
        createdAt = ZonedDateTime.parse(this.createdAt),
        updatedAt = ZonedDateTime.parse(this.updatedAt)
    )
}

fun TransactionResponseDto.toEntity(isSynced: Boolean): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        accountId = this.account.id,
        categoryId = this.category.id,
        amount = this.amount.toDoubleOrNull() ?: 0.0,
        transactionDate = this.transactionDate,
        comment = this.comment,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isSynced = isSynced,
        isDeleted = false
    )
}

fun TransactionWithDetails.toDomain(): Transaction? {
    if (this.account == null || this.category == null) {
        return null
    }
    return Transaction(
        id = this.transaction.id?.toString() ?: "local_${this.transaction.localId}",
        amount = this.transaction.amount,
        comment = this.transaction.comment,
        transactionDate = parseZonedDateTimeSafe(this.transaction.transactionDate),
        account = this.account.toDomainBrief(),
        category = this.category.toDomain(),
        createdAt = parseZonedDateTimeSafe(this.transaction.createdAt),
        updatedAt = parseZonedDateTimeSafe(this.transaction.updatedAt)
    )
}


private fun parseZonedDateTimeSafe(dateString: String): ZonedDateTime {
    return try {
        ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    } catch (e: DateTimeParseException) {
        Log.e("DateParsingError", "Failed to parse date: $dateString", e)
        ZonedDateTime.now()
    }
}

fun TransactionEntity.toRequestDto(): TransactionRequestDto {
    return TransactionRequestDto(
        accountId = this.accountId,
        categoryId = this.categoryId,
        transactionDate = this.transactionDate,
        amount = "%.2f".format(Locale.US, this.amount),
        comment = this.comment
    )
}