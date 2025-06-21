package com.coding.financialdetective.core.domain.util

sealed interface UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent
}