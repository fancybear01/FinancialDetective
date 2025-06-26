package com.coding.financialdetective.features.settings.ui

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val language: String = "English",
    val isLoading: Boolean = false
)