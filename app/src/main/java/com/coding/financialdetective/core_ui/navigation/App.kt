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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.features.acccount.ui.account_info.AccountScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coding.financialdetective.NavigationEvent
import com.coding.financialdetective.core_ui.common.components.AppBottomNavigationBar
import com.coding.financialdetective.core_ui.common.components.AppFloatingActionButton
import com.coding.financialdetective.core_ui.common.components.AppTopBar
import com.coding.financialdetective.core_ui.common.components.ConnectionError
import com.coding.financialdetective.core_ui.util.UiEvent
import com.coding.financialdetective.features.acccount.ui.editing_an_account.EditAccountScreen
import com.coding.financialdetective.features.categories.ui.CategoriesScreen
import com.coding.financialdetective.features.settings.ui.SettingsScreen
import com.coding.financialdetective.features.transactions.ui.expenses_incomes.ExpensesScreen
import com.coding.financialdetective.features.transactions.ui.expenses_incomes.IncomesScreen
import com.coding.financialdetective.features.transactions.ui.my_history.MyHistoryScreen
import kotlinx.coroutines.launch


val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

val LocalMainViewModel = staticCompositionLocalOf<MainViewModel> {
    error("No MainViewModel provided")
}

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
@Composable
fun NavController.currentRouteAsState(): String {
    val navBackStackEntry by currentBackStackEntryAsState()
    val routePattern = navBackStackEntry?.destination?.route

    return if (routePattern == "history/{isIncome}") {
        val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome")
        "history/$isIncome"
    } else {
        routePattern ?: stringResource(Screen.Expenses.routeResId)
    }
}

@Composable
inline fun <reified T : ViewModel> daggerViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory
): T = viewModel(modelClass = T::class.java, key = key, factory = factory)