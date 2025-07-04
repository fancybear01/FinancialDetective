package com.coding.financialdetective.features.categories.data.mapper

import com.coding.financialdetective.features.categories.data.remote.dto.CategoryDto
import com.coding.financialdetective.features.categories.domain.model.Category
import com.coding.financialdetective.features.categories.domain.model.CategoryType
import com.coding.financialdetective.features.categories.ui.CategoryUi


fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = if (this.isIncome) CategoryType.INCOME else CategoryType.EXPENSE
    )
}