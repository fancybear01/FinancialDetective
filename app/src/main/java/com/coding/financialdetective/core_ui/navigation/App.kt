package com.coding.financialdetective.core_ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.koin.androidx.compose.koinViewModel

//@Composable
//fun App() {
//    val navController = rememberNavController()
//    val hostState = remember { SnackbarHostState() }
//
//    val viewModel: MainViewModel = koinViewModel()
//    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//
//    LaunchedEffect(key1 = navController) {
//        launch {
//            viewModel.events.collect { event ->
//                when (event) {
//                    is UiEvent.ShowSnackbar -> {
//                        hostState.showSnackbar(message = event.message.asString(context))
//                    }
//                }
//            }
//        }
//
//        launch {
//            viewModel.navigationEvents.collect { event ->
//                when (event) {
//                    is NavigationEvent.NavigateBack -> {
//                        navController.popBackStack()
//                    }
//                }
//            }
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(hostState = hostState) },
//        topBar = {
//            Column {
//                AppTopBar(navController)
//                ConnectionError(!isConnected)
//            }
//        },
//        bottomBar = { AppBottomNavigationBar(navController) },
//        floatingActionButton = { AppFloatingActionButton(navController) }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = stringResource(Screen.Expenses.routeResId),
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable("expenses") { ExpensesScreen() }
//            composable("incomes") { IncomesScreen() }
//            composable("account") { AccountScreen() }
//            composable("categories") { CategoriesScreen() }
//            composable("settings") { SettingsScreen() }
//            composable(
//                route = "history/{isIncome}",
//                arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
//            ) { MyHistoryScreen() }
//            composable("edit_account") {
//                EditAccountScreen()
//            }
//        }
//    }
//}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Этот Composable теперь чистый и зависит только от стабильных параметров
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
fun App() {
    val navController = rememberNavController()
    val hostState = remember { SnackbarHostState() }

    val viewModel: MainViewModel = koinViewModel()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = navController) {
        launch {
            viewModel.events.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        hostState.showSnackbar(message = event.message.asString(context))
                    }
                }
            }
        }

        launch {
            viewModel.navigationEvents.collect { event ->
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
                AppTopBar(navController)
                // Этот Composable вызывает рекомпозицию Scaffold, но не должен влиять на контент
                ConnectionError(!isConnected)
            }
        },
        bottomBar = { AppBottomNavigationBar(navController) },
        floatingActionButton = { AppFloatingActionButton(navController) }
    ) { innerPadding ->
        // Вызываем наш новый, стабильный Composable
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
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