package com.coding.feature_settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core.domain.repository.HapticFeedbackEffect
import com.coding.core.domain.repository.LanguageSetting
import com.coding.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val state: StateFlow<SettingsState> = combine(
        userPreferencesRepository.isDarkTheme,
        userPreferencesRepository.colorSchemeSetting,
        userPreferencesRepository.hapticsEnabled,
        userPreferencesRepository.hapticEffect,
        userPreferencesRepository.languageSetting
    ) { isDark, scheme, hapticsOn, effect, language ->
        SettingsState(
            isDarkTheme = isDark,
            colorScheme = scheme,
            hapticsEnabled = hapticsOn,
            hapticEffect = effect,
            language = language
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val _commandChannel = Channel<SettingsCommand>()
    val commands = _commandChannel.receiveAsFlow()

    fun onThemeToggled(isDark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkTheme(isDark)
        }
    }

    fun onColorSchemeChanged(scheme: ColorSchemeSetting) {
        viewModelScope.launch {
            userPreferencesRepository.setColorSchemeSetting(scheme)
        }
    }

    fun onHapticsToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setHapticsEnabled(isEnabled)
        }
    }

    fun onHapticEffectChanged(effect: HapticFeedbackEffect) {
        viewModelScope.launch {
            userPreferencesRepository.setHapticEffect(effect)
        }
    }

    fun onLanguageChanged(language: LanguageSetting) {
        viewModelScope.launch {
            userPreferencesRepository.setLanguageSetting(language)
            _commandChannel.send(SettingsCommand.ApplyLanguage(language))
        }
    }
}

sealed class SettingsCommand {
    data class ApplyLanguage(val language: LanguageSetting) : SettingsCommand()
}