package com.coding.financialdetective.core_ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coding.financialdetective.MainViewModel
import com.coding.feature_accounts.ui.account_info.AccountScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.coding.financialdetective.NavigationEvent
import com.coding.core_ui.common.components.ConnectionError
import com.coding.core.util.UiEvent
import com.coding.core_ui.common.components.AppBottomNavigationBar
import com.coding.core_ui.common.components.AppFloatingActionButton
import com.coding.core_ui.common.components.AppTopBar
import com.coding.core_ui.navigation.Screen
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.navigation.LocalNavController
import com.coding.feature_accounts.ui.editing_an_account.EditAccountScreen
import com.coding.feature_categories.ui.CategoriesScreen
import com.coding.feature_settings.ui.SettingsScreen
import com.coding.feature_transactions.ui.expenses_incomes.ExpensesScreen
import com.coding.feature_transactions.ui.expenses_incomes.IncomesScreen
import com.coding.feature_transactions.ui.my_history.MyHistoryScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = stringResource(Screen.Expenses.routeResId),
        modifier = modifier
    ) {
        composable("expenses") { ExpensesScreen() }
        composable("incomes") { IncomesScreen() }
        composable("account") { AccountScreen() }
        composable("categories") { CategoriesScreen() }
        composable("settings") { SettingsScreen() }
        composable(
            route = "history/{isIncome}",
            arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
        ) { MyHistoryScreen() }
        composable("edit_account") {
            EditAccountScreen()
        }
    }
}

@Composable
fun App(mainViewModel: MainViewModel) {
    CompositionLocalProvider(LocalMainViewModel provides mainViewModel) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavController provides navController) {
            val hostState = remember { SnackbarHostState() }

            val isConnected by mainViewModel.isConnected.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(key1 = navController) {
                launch {
                    mainViewModel.events.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                hostState.showSnackbar(message = event.message.asString(context))
                            }
                        }
                    }
                }

                launch {
                    mainViewModel.navigationEvents.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = hostState) },
                topBar = {
                    Column {
                        AppTopBar(
                            navController = navController,
                            onTopBarAction = mainViewModel.onTopBarActionClick
                        )
                        ConnectionError(!isConnected)
                    }
                },
                bottomBar = { AppBottomNavigationBar(navController) },
                floatingActionButton = { AppFloatingActionButton(navController) }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}