package com.coding.financialdetective.navigation

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
            image = R.drawable.ic_categories,
            screen = Screen.SpendingItems
        ),
        BarItem(
            title = "Настройки",
            image = R.drawable.ic_settings,
            screen = Screen.Settings
        )
    )
}