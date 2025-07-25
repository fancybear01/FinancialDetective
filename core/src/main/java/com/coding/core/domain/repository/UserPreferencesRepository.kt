package com.coding.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Boolean>

    suspend fun setDarkTheme(isDark: Boolean)

    val colorSchemeSetting: Flow<ColorSchemeSetting>

    suspend fun setColorSchemeSetting(scheme: ColorSchemeSetting)

    val hapticsEnabled: Flow<Boolean>
    val hapticEffect: Flow<HapticFeedbackEffect>

    suspend fun setHapticsEnabled(enabled: Boolean)
    suspend fun setHapticEffect(effect: HapticFeedbackEffect)

    val languageSetting: Flow<LanguageSetting>
    suspend fun setLanguageSetting(language: LanguageSetting)
}

enum class ColorSchemeSetting {
    GREEN,
    BLUE,
    ORANGE,
    PURPLE
}

enum class HapticFeedbackEffect {
    LIGHT,
    MEDIUM,
    HEAVY
}

enum class LanguageSetting(val code: String) {
    RUSSIAN("ru"),
    ENGLISH("en")
}