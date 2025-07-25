package com.coding.feature_security.ui.pincode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.navigation.LocalNavController
import com.coding.feature_security.di.DaggerSecurityFeatureComponent

@Composable
fun PinCodeScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val securityFeatureComponent = remember {
        DaggerSecurityFeatureComponent.factory()
            .create(context.appDependencies)
    }

    val viewModel: PinCodeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return securityFeatureComponent.pinCodeViewModelFactory().create() as T
            }
        }
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.step) {
        if (state.step == PinScreenStep.PIN_SET_SUCCESS) {
            navController.popBackStack()
        }
    }

    PinCodeContent(
        state = state,
        onNumberClick = viewModel::onNumberClicked,
        onBackspaceClick = viewModel::onBackspaceClicked
    )
}

@Composable
private fun PinCodeContent(
    state: PinCodeState,
    onNumberClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = when (state.step) {
                PinScreenStep.CREATE_PIN -> "Создайте пин-код"
                PinScreenStep.CONFIRM_PIN -> "Подтвердите пин-код"
                PinScreenStep.PIN_MISMATCH -> "Пин-коды не совпали. Попробуйте снова."
                PinScreenStep.PIN_SET_SUCCESS -> "Пин-код установлен!"
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = if (state.step == PinScreenStep.PIN_MISMATCH) MaterialTheme.colorScheme.error else Color.Unspecified
        )

        PinIndicators(
            pinLength = state.pinLength,
            enteredCount = state.enteredPin.length
        )

        Numpad(
            onNumberClick = onNumberClick,
            onBackspaceClick = onBackspaceClick
        )
    }
}

@Composable
fun PinIndicators(
    pinLength: Int,
    enteredCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(pinLength) { index ->
            val isFilled = index < enteredCount
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isFilled) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}

@Composable
fun Numpad(
    onNumberClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttons = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "", "0", "⌫"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons) { symbol ->
            if (symbol.isEmpty()) {
                Spacer(Modifier.size(72.dp))
            } else {
                Button(
                    onClick = {
                        if (symbol == "⌫") {
                            onBackspaceClick()
                        } else {
                            onNumberClick(symbol.toInt())
                        }
                    },
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(text = symbol, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}