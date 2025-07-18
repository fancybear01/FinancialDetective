package com.coding.core_ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavController.currentRouteAsState(): String? {
    val navBackStackEntry by currentBackStackEntryAsState()
    val routePattern = navBackStackEntry?.destination?.route ?: return null

    return when (routePattern) {
        "analysis/{isIncome}" -> {
            val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome") ?: false
            "analysis/$isIncome"
        }
        "history/{isIncome}" -> {
            val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome") ?: false
            "history/$isIncome"
        }
        else -> routePattern.substringBefore('?')
    }
}