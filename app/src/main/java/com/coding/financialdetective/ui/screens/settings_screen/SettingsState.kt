package com.coding.financialdetective.ui.screens.settings_screen

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val language: String = "English",
    val isLoading: Boolean = false
)