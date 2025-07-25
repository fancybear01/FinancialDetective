package com.coding.feature_security.ui.auth

import androidx.lifecycle.ViewModel
import com.coding.core.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.asStateFlow

class PinCodeAuthViewModel @AssistedInject constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(): PinCodeAuthViewModel
    }

    private val _enteredPin = MutableStateFlow("")
    val enteredPin = _enteredPin.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError = _hasError.asStateFlow()

    fun onNumberClicked(number: Int) {
        if (hasError.value) {
            _enteredPin.value = ""
            _hasError.value = false
        }
        if (_enteredPin.value.length < 4) {
            _enteredPin.value += number
        }
    }

    fun onBackspaceClicked() {
        _enteredPin.value = _enteredPin.value.dropLast(1)
        _hasError.value = false
    }

    suspend fun checkPin(): Boolean {
        val isCorrect = securityRepository.checkPinCode(_enteredPin.value)
        if (!isCorrect) {
            _enteredPin.value = ""
            _hasError.value = true
        }
        return isCorrect
    }
}