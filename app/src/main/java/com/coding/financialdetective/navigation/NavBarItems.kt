package com.coding.financialdetective.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import com.coding.financialdetective.R

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Расходы",
            image = R.drawable.ic_expenses,
            screen = Screen.Expenses
        ),
        BarItem(
            title = "Доходы",
            image = R.drawable.ic_incomes,
            screen = Screen.Incomes
        ),
        BarItem(
            title = "Счёт",
            image = R.drawable.ic_account,
            screen = Screen.Account
        ),
        BarItem(
            title = "Статьи",
            image = R.drawable.ic_spending_items,
            screen = Screen.SpendingItems
        ),
        BarItem(
            title = "Настройки",
            image = R.drawable.is_settings,
            screen = Screen.Settings
        )
    )
}