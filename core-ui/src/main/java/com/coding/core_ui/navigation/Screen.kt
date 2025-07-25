package com.coding.core_ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.coding.core_ui.R
import com.coding.core_ui.navigation.icons.ActionIcon
import com.coding.core_ui.navigation.icons.BackNavigationIcon
import com.coding.core_ui.navigation.icons.BottomNavigationIcon

sealed class Screen(
    val routeResId: Int = -1,
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
        relatedRoutesResIds = listOf(
            R.string.expenses_history_route,
            R.string.expense_details_route,
            R.string.analysis_route_expenses
        ),
    )

    data object Incomes : Screen(
        routeResId = R.string.incomes_route,
        titleResId = R.string.incomes_header,
        action = ActionIcon.IncomesAction,
        bottomNavigationIcon = BottomNavigationIcon.IncomesIcon,
        relatedRoutesResIds = listOf(
            R.string.incomes_history_route,
            R.string.income_details_route,
            R.string.analysis_route_incomes
        )
    )

    data object Account : Screen(
        routeResId = R.string.account_route,
        titleResId = R.string.wallet_header,
        action = ActionIcon.AccountAction,
        bottomNavigationIcon = BottomNavigationIcon.AccountIcon,
        relatedRoutesResIds = listOf(R.string.edit_account_route)
    )

    data object Categories : Screen(
        routeResId = R.string.categories_route,
        titleResId = R.string.articles_header,
        bottomNavigationIcon = BottomNavigationIcon.CategoriesIcon
    )

    data object Settings : Screen(
        routeResId = R.string.settings_route,
        titleResId = R.string.settings_header,
        bottomNavigationIcon = BottomNavigationIcon.SettingsIcon,
        relatedRoutesResIds = listOf(
            R.string.pincode_setup_route,
            R.string.security_settings_route
        )
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

    data object EditAccount : Screen(
        routeResId = R.string.edit_account_route,
        titleResId = R.string.wallet_header,
        action = ActionIcon.EditAccountAction,
        backNavigationIcon = BackNavigationIcon.EditAccountBack
    )

    data object ExpenseDetails : Screen(
        routeResId = R.string.expense_details_route,
        titleResId = R.string.expense_details_title,
        action = ActionIcon.TransactionDetailsConfirmAction,
        backNavigationIcon = BackNavigationIcon.CancelAction
    )

    data object IncomeDetails : Screen(
        routeResId = R.string.income_details_route,
        titleResId = R.string.income_details_title,
        action = ActionIcon.TransactionDetailsConfirmAction,
        backNavigationIcon = BackNavigationIcon.CancelAction
    )

    data class Analysis(val isIncome: Boolean) : Screen(
        titleResId = R.string.analysis_header,
        backNavigationIcon = BackNavigationIcon.DefaultBack
    )

    data object PinCodeSetup : Screen(
        routeResId = R.string.pincode_setup_route,
        titleResId = R.string.pincode_setup_header,
        backNavigationIcon = BackNavigationIcon.DefaultBack
    )

    data object SecuritySettings : Screen(
        routeResId = R.string.security_settings_route, // ""
        titleResId = R.string.security_settings_header, // ""
        backNavigationIcon = BackNavigationIcon.DefaultBack
    )
}

fun getScreen(route: String?): Screen {
    if (route == null) return Screen.Expenses
    if (route.startsWith("history/")) {
        val isIncome = route.substringAfter("history/") == "true"
        return if (isIncome) Screen.IncomesHistory else Screen.ExpensesHistory
    }
    if (route.startsWith("analysis/")) {
        val isIncome = route.substringAfter("analysis/") == "true"
        return Screen.Analysis(isIncome)
    }
    val baseRoute = route.substringBefore('?')
    return when (baseRoute) {
        "expenses" -> Screen.Expenses
        "incomes" -> Screen.Incomes
        "account" -> Screen.Account
        "categories" -> Screen.Categories
        "settings" -> Screen.Settings
        "edit_account" -> Screen.EditAccount
        "expense_details" -> Screen.ExpenseDetails
        "income_details" -> Screen.IncomeDetails
        "pincode_setup" -> Screen.PinCodeSetup
        "security_settings" -> Screen.SecuritySettings
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