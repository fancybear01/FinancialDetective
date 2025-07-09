package com.coding.core_ui.navigation.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.coding.core_ui.R

sealed class BackNavigationIcon(
    val iconResId: Int,
    val routeResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getRoute() = stringResource(routeResId)

    data object ExpensesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.left_arrow,
        routeResId = R.string.expenses_route
    )

    data object IncomesHistoryBack : BackNavigationIcon(
        iconResId = R.drawable.left_arrow,
        routeResId = R.string.incomes_route
    )

    data object EditAccountBack : BackNavigationIcon(
        iconResId = R.drawable.ic_cancel,
        routeResId = R.string.account_route
    )
}