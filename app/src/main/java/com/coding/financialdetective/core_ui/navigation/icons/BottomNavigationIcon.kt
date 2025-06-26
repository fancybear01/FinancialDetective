package com.coding.financialdetective.core_ui.navigation.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.coding.financialdetective.R

sealed class BottomNavigationIcon(
    val iconResId: Int,
    val labelResId: Int
) {
    @Composable
    fun getIcon() = ImageVector.vectorResource(iconResId)

    @Composable
    fun getLabel() = stringResource(labelResId)

    data object ExpensesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_expenses,
        labelResId = R.string.expenses_icon
    )

    data object IncomesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_incomes,
        labelResId = R.string.incomes_icon
    )

    data object AccountIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_account,
        labelResId = R.string.wallet_icon
    )

    data object CategoriesIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_categories,
        labelResId = R.string.articles_icon
    )

    data object SettingsIcon : BottomNavigationIcon(
        iconResId = R.drawable.ic_settings,
        labelResId = R.string.settings_icon
    )
}