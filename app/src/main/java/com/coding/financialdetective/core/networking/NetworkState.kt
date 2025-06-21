package com.coding.financialdetective.core.networking

sealed interface NetworkState {
    object Initial : NetworkState

    object Available : NetworkState

    object Unavailable : NetworkState
}