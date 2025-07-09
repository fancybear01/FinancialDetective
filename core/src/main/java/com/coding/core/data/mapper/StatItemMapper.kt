package com.coding.core.data.mapper

import com.coding.core.data.dto.account_dto.StatItemDto
import com.coding.core.domain.model.account_models.StatItem

fun StatItemDto.toDomain(): StatItem {
    return StatItem(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        emoji = this.emoji,
        amount = this.amount.toDouble(),
    )
}