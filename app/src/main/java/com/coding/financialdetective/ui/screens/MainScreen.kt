package com.coding.financialdetective.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coding.financialdetective.R
import com.coding.financialdetective.navigation.BottomNavigationBar
import com.coding.financialdetective.navigation.NavBarItems
import com.coding.financialdetective.navigation.Screen
import com.coding.financialdetective.ui.components.TopBar
import com.coding.financialdetective.ui.screens.account_screen.AccountScreen
import com.coding.financialdetective.ui.screens.categories_screen.CategoriesScreen
import com.coding.financialdetective.ui.screens.expenses_screen.ExpensesScreen
import com.coding.financialdetective.ui.screens.incomes_screen.IncomesScreen
import com.coding.financialdetective.ui.screens.settings_screen.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = NavBarItems.BarItems.find {
        currentDestination?.route == it.screen::class.qualifiedName
    }?.screen
    Scaffold(
        topBar = {
            when (currentScreen) {
                is Screen.Expenses -> TopBar(text = "Расходы сегодня", iconEnd = R.drawable.ic_history)
                is Screen.Incomes -> TopBar(text = "Доходы сегодня", iconEnd = R.drawable.ic_history)
                is Screen.SpendingItems -> TopBar(text = "Мои статьи")
                is Screen.Account -> TopBar(text = "Мой счёт", iconEnd = R.drawable.ic_edit)
                is Screen.Settings -> TopBar(text = "Настройки")
                else -> {}
            }
        },
        floatingActionButton = {
            when (currentScreen) {
                is Screen.Expenses,
                is Screen.Incomes,
                is Screen.Account -> {
                    FloatingActionButton(
                        onClick = {
                            when (currentScreen) {
                                is Screen.Expenses -> { TODO() }
                                is Screen.Incomes -> { TODO() }
                                is Screen.Account -> { TODO() }
                                else -> {}
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(Icons.Default.Add, "Добавить")
                    }
                }
                else -> {}
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses,
            modifier = Modifier.padding(padding)
        ) {
            composable<Screen.Expenses> {
                ExpensesScreen()
            }
            composable<Screen.Incomes> {
                IncomesScreen()
            }
            composable<Screen.Account> {
                AccountScreen()
            }
            composable<Screen.SpendingItems> {
                CategoriesScreen()
            }
            composable<Screen.Settings> {
                SettingsScreen()
            }
        }
    }
}