package com.coding.financialdetective.mappers

import com.coding.financialdetective.models.data_models.CategoryDto
import com.coding.financialdetective.models.domain_models.Category
import com.coding.financialdetective.models.domain_models.CategoryType
import com.coding.financialdetective.models.ui_models.CategoryUi

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = if (this.isIncome) CategoryType.INCOME else CategoryType.EXPENSE
    )
}

fun Category.toUiModel(): CategoryUi {
    return CategoryUi(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = this.type
    )
}