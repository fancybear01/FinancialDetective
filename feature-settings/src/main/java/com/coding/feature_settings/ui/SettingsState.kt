package com.coding.feature_settings.ui

import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core.domain.repository.HapticFeedbackEffect
import com.coding.core.domain.repository.LanguageSetting

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val colorScheme: ColorSchemeSetting = ColorSchemeSetting.GREEN,
    val hapticsEnabled: Boolean = true,
    val hapticEffect: HapticFeedbackEffect = HapticFeedbackEffect.LIGHT,
    val language: LanguageSetting = LanguageSetting.RUSSIAN
)