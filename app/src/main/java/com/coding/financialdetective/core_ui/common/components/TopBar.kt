package com.coding.financialdetective.core_ui.common.components

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.appComponent
import com.coding.financialdetective.core_ui.navigation.Screen
import com.coding.financialdetective.core_ui.navigation.currentRouteAsState
import com.coding.financialdetective.core_ui.navigation.daggerViewModel
import com.coding.financialdetective.core_ui.navigation.getScreen

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

    val mainViewModel: MainViewModel = daggerViewModel(
        factory = LocalContext.current.appComponent.viewModelFactory()
    )


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