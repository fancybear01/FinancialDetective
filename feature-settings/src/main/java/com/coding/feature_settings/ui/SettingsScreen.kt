package com.coding.feature_settings.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.di.daggerViewModel
import com.coding.feature_settings.R

@Composable
fun SettingsScreen() {
    val dependencies = LocalContext.current.appDependencies

    val viewModel: SettingsViewModel = daggerViewModel(factory = dependencies.viewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn {
        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(title = "Светлая/темная тема"),
                    trail = TrailInfo.Switch(
                        isSwitched = state.isDarkTheme,
                        onSwitch = { isEnabled ->
                            viewModel.onThemeToggled(isEnabled)
                        }
                    )
                ),
                onClick = null,
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
                    trail = TrailInfo.Chevron(R.drawable.ic_thick_chevron)
                ),
                onClick = {},
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
            )
        }
    }
}