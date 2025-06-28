package com.coding.financialdetective.features.categories.ui

import com.coding.financialdetective.features.categories.domain.model.Category

fun Category.toUiModel(): CategoryUi {
    return CategoryUi(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = this.type
    )
}