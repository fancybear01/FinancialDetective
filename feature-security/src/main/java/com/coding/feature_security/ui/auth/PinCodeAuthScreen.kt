package com.coding.feature_security.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.core_ui.di.appDependencies
import com.coding.feature_security.di.DaggerSecurityFeatureComponent
import com.coding.feature_security.ui.pincode.Numpad
import com.coding.feature_security.ui.pincode.PinIndicators

@Composable
fun PinCodeAuthScreen(
    onPinCorrect: () -> Unit
) {
    val context = LocalContext.current

    val securityFeatureComponent = remember {
        DaggerSecurityFeatureComponent.factory()
            .create(context.appDependencies)
    }

    val viewModel: PinCodeAuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return securityFeatureComponent.pinCodeAuthViewModelFactory().create() as T
            }
        }
    )

    val enteredPin by viewModel.enteredPin.collectAsStateWithLifecycle()
    val hasError by viewModel.hasError.collectAsStateWithLifecycle()

    LaunchedEffect(enteredPin) {
        if (enteredPin.length == 4) {
            if (viewModel.checkPin()) {
                onPinCorrect()
            }
        }
    }

    PinCodeAuthContent(
        enteredPinCount = enteredPin.length,
        hasError = hasError,
        onNumberClick = viewModel::onNumberClicked,
        onBackspaceClick = viewModel::onBackspaceClicked
    )
}

@Composable
private fun PinCodeAuthContent(
    enteredPinCount: Int,
    hasError: Boolean,
    onNumberClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = if (hasError) "Неверный пин-код" else "Введите пин-код",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )

        PinIndicators(
            pinLength = 4,
            enteredCount = enteredPinCount
        )

        Numpad(
            onNumberClick = onNumberClick,
            onBackspaceClick = onBackspaceClick
        )
    }
}