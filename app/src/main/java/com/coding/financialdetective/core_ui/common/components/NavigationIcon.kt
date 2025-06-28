package com.coding.financialdetective.core_ui.common.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.coding.financialdetective.core_ui.navigation.currentRouteAsState
import com.coding.financialdetective.core_ui.navigation.getScreen

@Composable
fun AppNavigationIcon(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    val currentScreen = getScreen(currentDestination)

    val backNavigation = currentScreen.backNavigationIcon

    if (backNavigation != null) {
        val backNavigationIcon = backNavigation.getIcon()

        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = backNavigationIcon,
                contentDescription = null
            )
        }
    }
}