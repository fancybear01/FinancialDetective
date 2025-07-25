package com.coding.financialdetective.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coding.feature_accounts.ui.account_info.AccountScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.coding.core_ui.common.components.ConnectionError
import com.coding.core.util.UiEvent
import com.coding.core_ui.common.components.AppBottomNavigationBar
import com.coding.core_ui.common.components.AppFloatingActionButton
import com.coding.core_ui.common.components.AppTopBar
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.navigation.Screen
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.navigation.LocalNavController
import com.coding.core_ui.navigation.currentRouteAsState
import com.coding.core_ui.navigation.getScreen
import com.coding.core_ui.util.rememberHapticFeedbackManager
import com.coding.feature_accounts.ui.editing_an_account.EditAccountScreen
import com.coding.feature_categories.ui.CategoriesScreen
import com.coding.feature_security.ui.pincode.PinCodeScreen
import com.coding.feature_security.ui.settings.SecuritySettingsScreen
import com.coding.feature_settings.ui.SettingsScreen
import com.coding.feature_settings.ui.SettingsViewModel
import com.coding.feature_transactions.ui.analysis.AnalysisScreen
import com.coding.feature_transactions.ui.details.TransactionDetailsScreen
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
        composable(
            route = "expense_details?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            TransactionDetailsScreen(transactionId, false)
        }

        composable(
            route = "income_details?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            TransactionDetailsScreen(transactionId, true)
        }

        composable(
            route = "analysis/{isIncome}",
            arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false
            AnalysisScreen(isIncome = isIncome)
        }
        composable("pincode_setup") { PinCodeScreen() }
        composable("security_settings") { SecuritySettingsScreen() }
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

            val dependencies = LocalContext.current.appDependencies
            val settingsViewModel: SettingsViewModel = daggerViewModel(factory = dependencies.viewModelFactory())
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()

            val hapticManager = rememberHapticFeedbackManager(
                isEnabled = settingsState.hapticsEnabled,
                effect = settingsState.hapticEffect
            )

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
                        val currentRoute = navController.currentRouteAsState()
                        val currentScreen = getScreen(currentRoute)
                        val onTopBarAction = mainViewModel.onTopBarActionClick
                        val isActionEnabled by mainViewModel.isTopBarActionEnabled.collectAsState()
                        val topBarContainerColor = if (currentScreen is Screen.Analysis) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                        val actionRoute =
                            if (
                                currentScreen !is Screen.EditAccount &&
                                currentScreen !is Screen.ExpenseDetails &&
                                currentScreen !is Screen.IncomeDetails
                                ) {
                                currentScreen.action?.getRoute()
                            } else {
                                null
                            }

                        val onActionClick: () -> Unit = {
                            if (onTopBarAction != null) {
                                onTopBarAction.invoke()
                            } else {
                                if (currentScreen is Screen.ExpensesHistory || currentScreen is Screen.IncomesHistory) {
                                    val isIncome = currentScreen is Screen.IncomesHistory
                                    navController.navigate("analysis/$isIncome")
                                } else {
                                    actionRoute?.let { route ->
                                        navController.navigate(route)
                                    }
                                }
                            }
                        }

                        val onActionClickWithHaptics: () -> Unit = {
                            hapticManager.performHapticFeedback()
                            onActionClick()
                        }

                        val onNavigateUpWithHaptics: () -> Unit = {
                            hapticManager.performHapticFeedback()
                            navController.navigateUp()
                        }

                        AppTopBar(
                            currentScreen = currentScreen,
                            containerColor = topBarContainerColor,
                            onNavigateUp = onNavigateUpWithHaptics,
                            onActionClick = onActionClickWithHaptics,
                            isActionEnabled = if (
                                currentScreen is Screen.EditAccount ||
                                currentScreen is Screen.ExpenseDetails ||
                                currentScreen is Screen.IncomeDetails
                                ) {
                                isActionEnabled
                            } else {
                                true
                            }
                        )
                        ConnectionError(!isConnected)
                    }
                },
                bottomBar = { AppBottomNavigationBar(navController, hapticManager) },
                floatingActionButton = {
                    val currentRoute = navController.currentRouteAsState()
                    val currentScreen = getScreen(currentRoute)

                    if (currentScreen is Screen.Expenses || currentScreen is Screen.Incomes) {
                        AppFloatingActionButton(
                            onClick = {
                                val route = if (currentScreen is Screen.Incomes)
                                    "income_details"
                                else
                                    "expense_details"
                                navController.navigate(route)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}