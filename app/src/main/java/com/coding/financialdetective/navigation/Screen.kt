package com.coding.financialdetective.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    val route: String

    @Serializable
    data object Expenses : Screen {
        override val route = "expenses"
    }

    @Serializable
    data object Incomes : Screen {
        override val route = "incomes"
    }

    @Serializable
    data object Account : Screen {
        override val route = "account"
    }

    @Serializable
    data object SpendingItems : Screen {
        override val route = "spending_items"
    }

    @Serializable
    data object Settings : Screen {
        override val route = "settings"
    }

    @Serializable
    data object MyHistory : Screen {
        override val route = "history"
    }
}