package com.coding.financialdetective.navigation

sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object Expenses : Screen

    @kotlinx.serialization.Serializable
    data object Incomes : Screen

    @kotlinx.serialization.Serializable
    data object Account : Screen

    @kotlinx.serialization.Serializable
    data object SpendingItems : Screen

    @kotlinx.serialization.Serializable
    data object Settings : Screen
}