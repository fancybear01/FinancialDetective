package com.coding.core_ui.navigation.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.coding.core_ui.R

sealed class ActionIcon(
    val iconResId: Int,
    val routeResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getRoute() = stringResource(routeResId)

    data object ExpensesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.expenses_history_route
    )

    data object IncomesAction : ActionIcon(
        iconResId = R.drawable.ic_history,
        routeResId = R.string.incomes_history_route
    )

    data object AccountAction : ActionIcon(
        iconResId = R.drawable.ic_edit,
        routeResId = R.string.edit_account_route
    )

    data object ExpensesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_analysis,
        routeResId = R.string.expenses_history_route
    )

    data object IncomesHistoryAction : ActionIcon(
        iconResId = R.drawable.ic_analysis,
        routeResId = R.string.incomes_history_route
    )

    data object EditAccountAction : ActionIcon(
        iconResId = R.drawable.ic_confirm,
        routeResId = R.string.account_route
    )
}