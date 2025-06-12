package com.coding.financialdetective.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coding.financialdetective.ui.theme.Gray
import com.coding.financialdetective.ui.theme.Green
import com.coding.financialdetective.ui.theme.MintGreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentDestination?.route == navItem.screen::class.qualifiedName,
                onClick = {
                    navController.navigate(navItem.screen) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.image),
                        contentDescription = navItem.title,
                        tint = if (currentDestination?.route == navItem.screen::class.qualifiedName) Green else Gray
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}