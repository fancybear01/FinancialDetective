package com.coding.core.data.mapper

import com.coding.core.data.local.entity.CategoryEntity
import com.coding.core.data.remote.dto.categoties_dto.CategoryDto
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.domain.model.categories_models.CategoryType


fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = if (this.isIncome) CategoryType.INCOME else CategoryType.EXPENSE
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(id = this.categoryId, name = this.name, emoji = this.emoji, type = CategoryType.valueOf(this.type))
}

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        categoryId = this.id,
        name = this.name,
        emoji = this.emoji,
        type = if (this.isIncome) "INCOME" else "EXPENSE"
    )
}