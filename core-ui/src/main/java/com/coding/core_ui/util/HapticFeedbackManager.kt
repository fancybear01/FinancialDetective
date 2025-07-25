package com.coding.core_ui.util

import android.os.Build
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import com.coding.core.domain.repository.HapticFeedbackEffect

interface HapticFeedbackManager {
    fun performHapticFeedback()
}

@Composable
fun rememberHapticFeedbackManager(
    isEnabled: Boolean,
    effect: HapticFeedbackEffect
): HapticFeedbackManager {
    val view = LocalView.current
    return remember(view, isEnabled, effect) {
        if (isEnabled) {
            DefaultHapticFeedbackManager(view, effect)
        } else {
            NoOpHapticFeedbackManager
        }
    }
}

private class DefaultHapticFeedbackManager(
    private val view: View,
    private val effect: HapticFeedbackEffect
) : HapticFeedbackManager {
    override fun performHapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hapticConstant = when (effect) {
                HapticFeedbackEffect.LIGHT -> android.view.HapticFeedbackConstants.VIRTUAL_KEY
                HapticFeedbackEffect.MEDIUM -> android.view.HapticFeedbackConstants.KEYBOARD_TAP
                HapticFeedbackEffect.HEAVY -> android.view.HapticFeedbackConstants.LONG_PRESS
            }
            view.performHapticFeedback(hapticConstant)
        } else {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }
}

private object NoOpHapticFeedbackManager : HapticFeedbackManager {
    override fun performHapticFeedback() { /* Do nothing */ }
}