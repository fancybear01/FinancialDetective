package com.coding.financialdetective.core_ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.coding.financialdetective.R
import com.coding.financialdetective.core_ui.navigation.icons.ActionIcon
import com.coding.financialdetective.core_ui.navigation.icons.BackNavigationIcon
import com.coding.financialdetective.core_ui.navigation.icons.BottomNavigationIcon

sealed class Screen(
    val routeResId: Int,
    val titleResId: Int,
    val action: ActionIcon? = null,
    val bottomNavigationIcon: BottomNavigationIcon? = null,
    val backNavigationIcon: BackNavigationIcon? = null,
    val relatedRoutesResIds: List<Int> = emptyList()
) {
    @Composable
    fun getRoute() = stringResource(routeResId)

    @Composable
    fun getTitle() = stringResource(titleResId)

    data object Expenses : Screen(
        routeResId = R.string.expenses_route,
        titleResId = R.string.expenses_header,
        action = ActionIcon.ExpensesAction,
        bottomNavigationIcon = BottomNavigationIcon.ExpensesIcon,
        relatedRoutesResIds = listOf(R.string.expenses_history_route)
    )

    data object Incomes : Screen(
        routeResId = R.string.incomes_route,
        titleResId = R.string.incomes_header,
        action = ActionIcon.IncomesAction,
        bottomNavigationIcon = BottomNavigationIcon.IncomesIcon,
        relatedRoutesResIds = listOf(R.string.incomes_history_route)
    )

    data object Account : Screen(
        routeResId = R.string.account_route,
        titleResId = R.string.wallet_header,
        action = ActionIcon.AccountAction,
        bottomNavigationIcon = BottomNavigationIcon.AccountIcon
    )

    data object Categories : Screen(
        routeResId = R.string.categories_route,
        titleResId = R.string.articles_header,
        bottomNavigationIcon = BottomNavigationIcon.CategoriesIcon
    )

    data object Settings : Screen(
        routeResId = R.string.settings_route,
        titleResId = R.string.settings_header,
        bottomNavigationIcon = BottomNavigationIcon.SettingsIcon
    )

    data object ExpensesHistory : Screen(
        routeResId = R.string.expenses_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.ExpensesHistoryAction,
        backNavigationIcon = BackNavigationIcon.ExpensesHistoryBack
    )

    data object IncomesHistory : Screen(
        routeResId = R.string.incomes_history_route,
        titleResId = R.string.history_header,
        action = ActionIcon.IncomesHistoryAction,
        backNavigationIcon = BackNavigationIcon.IncomesHistoryBack
    )
}

fun getScreen(route: String): Screen {
    return when (route) {
        "expenses" -> Screen.Expenses
        "incomes" -> Screen.Incomes
        "account" -> Screen.Account
        "categories" -> Screen.Categories
        "settings" -> Screen.Settings
        "history/false" -> Screen.ExpensesHistory
        "history/true" -> Screen.IncomesHistory
        else -> Screen.Expenses
    }
}

val screens = listOf(
    Screen.Expenses,
    Screen.Incomes,
    Screen.Account,
    Screen.Categories,
    Screen.Settings
)