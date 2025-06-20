package com.coding.financialdetective.navigation

import com.coding.financialdetective.navigation.Screen.MyHistory.Companion.baseRoute
import kotlinx.serialization.Serializable

sealed interface Screen {
    val route: String
        get() = when (this) {
            is MyHistory -> baseRoute
            else -> this::class.simpleName ?: ""
        }

    @Serializable
    data object Expenses : Screen

    @Serializable
    data object Incomes : Screen

    @Serializable
    data object Account : Screen

    @Serializable
    data object SpendingItems : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data class MyHistory(val transactionType: String) : Screen {
        companion object {
            const val baseRoute = "MyHistory"
        }
    }
}