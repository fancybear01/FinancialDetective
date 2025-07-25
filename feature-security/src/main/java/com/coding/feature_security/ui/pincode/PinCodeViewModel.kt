package com.coding.feature_security.ui.pincode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.domain.repository.SecurityRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinCodeViewModel @AssistedInject constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(): PinCodeViewModel
    }

    private val _state = MutableStateFlow(PinCodeState())
    val state: StateFlow<PinCodeState> = _state

    fun onNumberClicked(number: Int) {
        if (_state.value.enteredPin.length < _state.value.pinLength) {
            _state.update { it.copy(enteredPin = it.enteredPin + number) }
            if (_state.value.enteredPin.length == _state.value.pinLength) {
                processPin()
            }
        }
    }

    fun onBackspaceClicked() {
        if (_state.value.enteredPin.isNotEmpty()) {
            _state.update { it.copy(enteredPin = it.enteredPin.dropLast(1)) }
        }
    }

    private fun processPin() {
        when (_state.value.step) {
            PinScreenStep.CREATE_PIN, PinScreenStep.PIN_MISMATCH -> {
                _state.update { it.copy(
                    step = PinScreenStep.CONFIRM_PIN,
                    firstPin = it.enteredPin,
                    enteredPin = ""
                )}
            }
            PinScreenStep.CONFIRM_PIN -> {
                if (_state.value.enteredPin == _state.value.firstPin) {
                    savePinCode(_state.value.enteredPin)
                } else {
                    _state.update { it.copy(
                        step = PinScreenStep.PIN_MISMATCH,
                        enteredPin = ""
                    )}
                }
            }
            else -> {}
        }
    }

    private fun savePinCode(pin: String) {
        viewModelScope.launch {
            securityRepository.setPinCode(pin)
            _state.update { it.copy(step = PinScreenStep.PIN_SET_SUCCESS) }
        }
    }

    fun clearPinCode() {
        viewModelScope.launch {
            securityRepository.clearPinCode()
        }
    }
}