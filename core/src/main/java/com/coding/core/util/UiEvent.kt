package com.coding.core.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent
}