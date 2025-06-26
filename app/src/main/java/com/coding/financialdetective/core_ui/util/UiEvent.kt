package com.coding.financialdetective.core_ui.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent
}