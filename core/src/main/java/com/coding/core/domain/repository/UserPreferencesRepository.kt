package com.coding.core.domain.repository

import kotlinx.coroutines.flow.Flow

enum class ThemeSetting {
    LIGHT, DARK, SYSTEM_DEFAULT
}

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Boolean>

    suspend fun setDarkTheme(isDark: Boolean)
}