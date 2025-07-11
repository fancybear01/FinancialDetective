package com.coding.core_ui.model.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.core.domain.model.categories_models.Category
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.LeadInfo
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.model.CategoryUi

fun Category.toUiModel(): CategoryUi {
    return CategoryUi(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        type = this.type
    )
}

@Composable
fun CategoryUi.toListItemModel(
    containerColorForIcon: Color = MaterialTheme.colorScheme.secondary
): ListItemModel {
    return ListItemModel(
        lead = LeadInfo(
            emoji = this.emoji,
            containerColorForIcon = containerColorForIcon
        ),
        content = ContentInfo(
            title = this.name
        )
    )
}