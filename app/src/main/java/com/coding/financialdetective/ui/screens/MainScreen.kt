package com.coding.financialdetective.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.R
import com.coding.financialdetective.core.domain.util.UiEvent
import com.coding.financialdetective.core.presentation.util.ObserveAsEvents
import com.coding.financialdetective.navigation.BottomNavigationBar
import com.coding.financialdetective.navigation.Screen
import com.coding.financialdetective.ui.components.TopBar
import com.coding.financialdetective.ui.screens.account_screen.AccountScreen
import com.coding.financialdetective.ui.screens.categories_screen.CategoriesScreen
import com.coding.financialdetective.ui.screens.expenses_incomes_screens.ExpensesScreen
import com.coding.financialdetective.ui.screens.expenses_incomes_screens.IncomesScreen
import com.coding.financialdetective.ui.screens.my_history_screen.MyHistoryScreen
import com.coding.financialdetective.ui.screens.settings_screen.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedItem = remember { mutableStateOf<Screen>(Screen.Expenses) }

    val showBanner by mainViewModel.shouldShowNetworkErrorBanner.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    ObserveAsEvents(events = mainViewModel.events) { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
            }
        }
    }

    val currentScreen: Screen? = when {
        currentDestination?.route?.startsWith(Screen.Expenses.route) == true -> Screen.Expenses
        currentDestination?.route?.startsWith(Screen.Incomes.route) == true -> Screen.Incomes
        currentDestination?.route?.startsWith(Screen.Account.route) == true -> Screen.Account
        currentDestination?.route?.startsWith(Screen.SpendingItems.route) == true -> Screen.SpendingItems
        currentDestination?.route?.startsWith(Screen.Settings.route) == true -> Screen.Settings
        currentDestination?.route?.startsWith(Screen.MyHistory.baseRoute) == true -> {
            val route = currentDestination.route ?: ""
            val transactionType = if (route.contains('/')) {
                route.substringAfter('/')
            } else {
                "expense"
            }
            Screen.MyHistory(transactionType)
        }

        else -> null
    }

    LaunchedEffect(currentScreen) {
        if (currentScreen != null && currentScreen !is Screen.MyHistory) {
            selectedItem.value = currentScreen
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column {
                when (currentScreen) {
                    is Screen.Expenses -> TopBar(
                        text = "Расходы сегодня",
                        iconEnd = R.drawable.ic_history,
                        onEndIconClick = { navController.navigate("${Screen.MyHistory.baseRoute}/expense") }
                    )

                    is Screen.Incomes -> TopBar(
                        text = "Доходы сегодня",
                        iconEnd = R.drawable.ic_history,
                        onEndIconClick = { navController.navigate("${Screen.MyHistory.baseRoute}/income") }
                    )

                    is Screen.SpendingItems -> TopBar(text = "Мои статьи")
                    is Screen.Account -> TopBar(
                        text = "Мой счёт",
                        iconEnd = R.drawable.ic_edit
                    )

                    is Screen.Settings -> TopBar(text = "Настройки")
                    is Screen.MyHistory -> TopBar(
                        text = "Моя история",
                        iconStart = R.drawable.left_arrow,
                        iconEnd = R.drawable.ic_analysis,
                        onStartIconClick = { navController.popBackStack() }
                    )

                    else -> {}
                }
                AnimatedVisibility(visible = showBanner) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.error)
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Отсутствует подключение к интернету",
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            when (currentScreen) {
                is Screen.Expenses,
                is Screen.Incomes,
                is Screen.Account -> {
                    FloatingActionButton(
                        onClick = { /* TODO */ },
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
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItem = selectedItem.value
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Expenses.route) {
                ExpensesScreen()
            }
            composable(Screen.Incomes.route) {
                IncomesScreen()
            }
            composable(Screen.Account.route) {
                AccountScreen()
            }
            composable(Screen.SpendingItems.route) {
                CategoriesScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = "${Screen.MyHistory.baseRoute}/{transactionType}",
                arguments = listOf(
                    navArgument("transactionType") {
                        type = NavType.StringType
                        defaultValue = "expense"
                    }
                )) { backStackEntry ->
                val transactionType =
                    backStackEntry.arguments?.getString("transactionType") ?: "expense"
                MyHistoryScreen(transactionType = transactionType)
            }
        }
    }
}
