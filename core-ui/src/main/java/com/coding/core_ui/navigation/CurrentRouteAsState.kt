package com.coding.core_ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavController.currentRouteAsState(): String {
    val navBackStackEntry by currentBackStackEntryAsState()
    val routePattern = navBackStackEntry?.destination?.route

    return if (routePattern == "history/{isIncome}") {
        val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome")
        "history/$isIncome"
    } else {
        routePattern ?: stringResource(Screen.Expenses.routeResId)
    }
}