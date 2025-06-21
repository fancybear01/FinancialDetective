package com.coding.financialdetective.mappers

import com.coding.financialdetective.models.data_models.StatItemDto
import com.coding.financialdetective.models.domain_models.StatItem

fun StatItemDto.toDomain(): StatItem {
    return StatItem(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        emoji = this.emoji,
        amount = this.amount.toDouble(),
    )
}