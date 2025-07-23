package com.coding.core_ui.common.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.coding.core_ui.navigation.currentRouteAsState
import com.coding.core_ui.navigation.screens
import com.coding.core_ui.util.HapticFeedbackManager

@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    hapticManager: HapticFeedbackManager
) {
    val currentDestination = navController.currentRouteAsState()

    NavigationBar  {
        screens.forEach { screen ->
            val screenRoute = screen.getRoute()
            val relatedRoutes = screen.relatedRoutesResIds.map { stringResource(it) }

            val bottomNavigationIcon = screen.bottomNavigationIcon!!.getIcon()
            val bottomNavigationLabel = screen.bottomNavigationIcon.getLabel()


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
                    hapticManager.performHapticFeedback()
                    navController.navigate(screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}