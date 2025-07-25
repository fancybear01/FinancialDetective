package com.coding.feature_security.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.domain.repository.SecurityRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SecuritySettingsViewModel @AssistedInject constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(): SecuritySettingsViewModel
    }

    val hasPinCode: StateFlow<Boolean> = securityRepository.hasPinCode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun clearPinCode() {
        viewModelScope.launch {
            securityRepository.clearPinCode()
        }
    }
}