package com.coding.feature_settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core.domain.repository.HapticFeedbackEffect
import com.coding.core.domain.repository.LanguageSetting
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.theme.BluePrimary
import com.coding.core_ui.theme.GreenPrimary
import com.coding.core_ui.theme.OrangePrimary
import com.coding.core_ui.theme.PurplePrimary
import com.coding.feature_settings.R

@Composable
fun SettingsScreen() {
    val dependencies = LocalContext.current.appDependencies
    val viewModel: SettingsViewModel = daggerViewModel(factory = dependencies.viewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onThemeToggled = viewModel::onThemeToggled,
        onColorSchemeChanged = viewModel::onColorSchemeChanged,
        onHapticsToggled = viewModel::onHapticsToggled,
        onHapticEffectChanged = viewModel::onHapticEffectChanged,
        onLanguageChanged = viewModel::onLanguageChanged
    )
}

@Composable
private fun SettingsContent(
    state: SettingsState,
    onThemeToggled: (Boolean) -> Unit,
    onColorSchemeChanged: (ColorSchemeSetting) -> Unit,
    onHapticsToggled: (Boolean) -> Unit,
    onHapticEffectChanged: (HapticFeedbackEffect) -> Unit,
    onLanguageChanged: (LanguageSetting) -> Unit
) {
    var showColorDialog by remember { mutableStateOf(false) }
    var showHapticEffectDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(title = "Темная тема"),
                    trail = TrailInfo.Switch(
                        isSwitched = state.isDarkTheme,
                        onSwitch = onThemeToggled
                    )
                ),
                onClick = null,
                modifier = Modifier
                    .defaultMinSize(56.dp)
            )
        }

        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(
                        title = "Основной цвет",
                        subtitle = when (state.colorScheme) {
                            ColorSchemeSetting.GREEN -> "Зеленый"
                            ColorSchemeSetting.BLUE -> "Синий"
                            ColorSchemeSetting.ORANGE -> "Оранжевый"
                            ColorSchemeSetting.PURPLE -> "Фиолетовый"
                        }
                    ),
                    trail = TrailInfo.Chevron()
                ),
                onClick = { showColorDialog = true },
                modifier = Modifier
                    .defaultMinSize(56.dp)
            )
        }

        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(title = "Вибрация"),
                    trail = TrailInfo.Switch(
                        isSwitched = state.hapticsEnabled,
                        onSwitch = onHapticsToggled
                    )
                ),
                onClick = null,
                modifier = Modifier
                    .defaultMinSize(56.dp)
            )
        }

        if (state.hapticsEnabled) {
            item {
                ListItem(
                    model = ListItemModel(
                        content = ContentInfo(
                            title = "Эффект вибрации",
                            subtitle = when (state.hapticEffect) {
                                HapticFeedbackEffect.LIGHT -> "Легкий"
                                HapticFeedbackEffect.MEDIUM -> "Средний"
                                HapticFeedbackEffect.HEAVY -> "Тяжелый"
                            }
                        ),
                        trail = TrailInfo.Chevron()
                    ),
                    onClick = { showHapticEffectDialog = true },
                    modifier = Modifier
                        .defaultMinSize(56.dp)
                )
            }
        }

        item {
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(
                        title = "Язык",
                        subtitle = when (state.language) {
                            LanguageSetting.RUSSIAN -> "Русский"
                            LanguageSetting.ENGLISH -> "English"
                        }
                    ),
                    trail = TrailInfo.Chevron()
                ),
                onClick = { showLanguageDialog = true }
            )
        }
    }

    if (showColorDialog) {
        ColorSelectionDialog(
            currentColorScheme = state.colorScheme,
            onDismiss = { showColorDialog = false },
            onSchemeSelected = { scheme ->
                onColorSchemeChanged(scheme)
                showColorDialog = false
            }
        )
    }

    if (showHapticEffectDialog) {
        HapticEffectSelectionDialog(
            currentEffect = state.hapticEffect,
            onDismiss = { showHapticEffectDialog = false },
            onEffectSelected = { effect ->
                onHapticEffectChanged(effect)
                showHapticEffectDialog = false
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = state.language,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { lang ->
                onLanguageChanged(lang)
                showLanguageDialog = false
            }
        )
    }
}

@Composable
private fun ColorSelectionDialog(
    currentColorScheme: ColorSchemeSetting,
    onDismiss: () -> Unit,
    onSchemeSelected: (ColorSchemeSetting) -> Unit,

) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите цвет") },
        text = {
            Column {
                ColorSchemeSetting.values().forEach { scheme ->
                    val (color, name) = when(scheme) {
                        ColorSchemeSetting.GREEN -> GreenPrimary to "Зеленый"
                        ColorSchemeSetting.BLUE -> BluePrimary to "Синий"
                        ColorSchemeSetting.ORANGE -> OrangePrimary to "Оранжевый"
                        ColorSchemeSetting.PURPLE -> PurplePrimary to "Фиолетовый"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (scheme == currentColorScheme),
                                onClick = { onSchemeSelected(scheme) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (scheme == currentColorScheme),
                            onClick = { onSchemeSelected(scheme) }
                        )
                        Spacer(Modifier.width(16.dp))

                        Box(Modifier.size(24.dp).background(color, CircleShape))

                        Spacer(Modifier.width(16.dp))

                        Text(name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun HapticEffectSelectionDialog(
    currentEffect: HapticFeedbackEffect,
    onDismiss: () -> Unit,
    onEffectSelected: (HapticFeedbackEffect) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите эффект вибрации") },
        text = {
            Column {
                HapticFeedbackEffect.values().forEach { effect ->
                    val effectName = when (effect) {
                        HapticFeedbackEffect.LIGHT -> "Легкий"
                        HapticFeedbackEffect.MEDIUM -> "Средний"
                        HapticFeedbackEffect.HEAVY -> "Тяжелый"
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (effect == currentEffect),
                                onClick = { onEffectSelected(effect) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (effect == currentEffect),
                            onClick = { onEffectSelected(effect) }
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(effectName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: LanguageSetting,
    onDismiss: () -> Unit,
    onLanguageSelected: (LanguageSetting) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите язык") },
        text = {
            Column {
                LanguageSetting.values().forEach { language ->
                    val languageName = when (language) {
                        LanguageSetting.RUSSIAN -> "Русский"
                        LanguageSetting.ENGLISH -> "English"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (language == currentLanguage),
                                onClick = { onLanguageSelected(language) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (language == currentLanguage),
                            onClick = { onLanguageSelected(language) }
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(languageName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}