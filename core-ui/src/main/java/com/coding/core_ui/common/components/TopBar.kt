package com.coding.core_ui.common.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.coding.core_ui.navigation.Screen
import com.coding.core_ui.navigation.currentRouteAsState
import com.coding.core_ui.navigation.getScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    onTopBarAction: (() -> Unit)?
) {

    val currentDestination = navController.currentRouteAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val currentScreen = getScreen(currentDestination)

    val title = currentScreen.getTitle()
    val action = currentScreen.action
    val backNavigation = currentScreen.backNavigationIcon

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (backNavigation != null) {
                AppNavigationIcon(navController)
            }
        },
        actions = {
            if (action != null) {

                val actionRoute = action.getRoute()

                val onClickAction = {
                    onTopBarAction?.invoke()
                    if (currentScreen !is Screen.EditAccount) {
                        navController.navigate(actionRoute)
                    }
                }

                val isEnabled = if (currentScreen is Screen.EditAccount) {
                    onTopBarAction != null
                } else {
                    true
                }

                IconButton(
                    onClick = onClickAction,
                    enabled = isEnabled
                ) {
                    Icon(
                        imageVector = action.getIcon(),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}