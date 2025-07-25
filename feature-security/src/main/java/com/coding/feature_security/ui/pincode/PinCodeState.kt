package com.coding.feature_security.ui.pincode

data class PinCodeState(
    val step: PinScreenStep = PinScreenStep.CREATE_PIN,
    val enteredPin: String = "",
    val firstPin: String = "",
    val pinLength: Int = 4,
    val error: String? = null
)