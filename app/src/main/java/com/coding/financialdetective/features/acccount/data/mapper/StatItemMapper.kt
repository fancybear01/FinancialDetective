package com.coding.financialdetective.features.acccount.data.mapper

import com.coding.financialdetective.features.acccount.data.remote.dto.StatItemDto
import com.coding.financialdetective.features.acccount.domain.model.StatItem

fun StatItemDto.toDomain(): StatItem {
    return StatItem(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        emoji = this.emoji,
        amount = this.amount.toDouble(),
    )
}