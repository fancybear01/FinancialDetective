package com.coding.financialdetective.models.ui_models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.coding.financialdetective.R

data class ListItemModel(
    val lead: LeadInfo? = null,
    val content: ContentInfo,
    val trail: TrailInfo? = null,
    val onClick: () -> Unit = {}
)

data class LeadInfo(
    val emoji: String,
    val containerColorForIcon: Color
)

data class ContentInfo(
    val title: String,
    val subtitle: String? = null
)

sealed interface TrailInfo {
    data class Value(
        val title: String,
        val subtitle: String? = null,
    ) : TrailInfo
    data class ValueAndChevron(
        val title: String,
        val subtitle: String? = null,
        @DrawableRes val chevronIcon: Int = R.drawable.ic_thin_chevron
    ) : TrailInfo
    data class Chevron(
        @DrawableRes val chevronIcon: Int = R.drawable.ic_thin_chevron
    ) : TrailInfo
    data class Switch(
        val isSwitched: Boolean,
        val onSwitch: (Boolean) -> Unit
    ) : TrailInfo
}