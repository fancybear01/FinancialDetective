package com.coding.financialdetective.ui.screens.settings_screen

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.financialdetective.R
import com.coding.financialdetective.models.ContentInfo
import com.coding.financialdetective.models.ListItemModel
import com.coding.financialdetective.models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.ui.screens.incomes_screen.IncomesViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn {
        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(title = "Светлая/темная тема"),
                    trail = TrailInfo.Switch(
                        isSwitched = state.isDarkTheme,
                        onSwitch = { isEnabled ->
                            viewModel.onAutoThemeToggled(isEnabled)
                        }
                    )
                ),
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
            )
        }

        val settingItems = listOf(
            "Основной цвет", "Звуки", "Хаптики", "Код пароль", "Синхронизация", "Язык", "О программе"
        )

        items(settingItems.size) { index ->
            val title = settingItems[index]
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(title = title),
                    trail = TrailInfo.Chevron(R.drawable.ic_thick_chevron),
                    onClick = {

                    }
                ),
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
            )
        }
    }
}