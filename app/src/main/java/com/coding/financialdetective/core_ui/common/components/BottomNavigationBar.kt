package com.coding.financialdetective.core_ui.common.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.coding.financialdetective.core_ui.navigation.currentRouteAsState
import com.coding.financialdetective.core_ui.navigation.screens

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    BottomAppBar {
        screens.forEach { screen ->
            val screenRoute = screen.getRoute()

            val bottomNavigationIcon = screen.bottomNavigationIcon!!.getIcon()
            val bottomNavigationLabel = screen.bottomNavigationIcon.getLabel()

            val relatedRoutes = screen.relatedRoutesResIds.map { stringResource(it) }

            val isSelected = currentDestination == screenRoute || relatedRoutes.contains(currentDestination)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = bottomNavigationIcon,
                        contentDescription = bottomNavigationLabel
                    )
                },
                selected = isSelected,
                label = {
                    Text(
                        text = bottomNavigationLabel,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.outline,
                    unselectedIconColor = MaterialTheme.colorScheme.outline
                ),
                onClick = {
                    navController.navigate(screenRoute) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}