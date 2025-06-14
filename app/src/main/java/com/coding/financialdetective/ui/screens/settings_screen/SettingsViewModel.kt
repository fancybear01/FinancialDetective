package com.coding.financialdetective.ui.screens.settings_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadInitialSettings()
    }

    fun onAutoThemeToggled(isEnabled: Boolean) {

    }

    private fun loadInitialSettings() {

    }


}