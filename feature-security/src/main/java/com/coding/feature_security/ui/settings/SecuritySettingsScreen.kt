package com.coding.feature_security.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core_ui.navigation.LocalNavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.core_ui.di.appDependencies
import com.coding.feature_security.di.DaggerSecurityFeatureComponent

@Composable
fun SecuritySettingsScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val securityFeatureComponent = remember {
        DaggerSecurityFeatureComponent.factory()
            .create(context.appDependencies)
    }

    val viewModel: SecuritySettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return securityFeatureComponent.securitySettingsViewModelFactory().create() as T
            }
        }
    )

    val hasPin by viewModel.hasPinCode.collectAsStateWithLifecycle()

    SecuritySettingsContent(
        hasPinCode = hasPin,
        onSetPinClick = { navController.navigate("pincode_setup") },
        onChangePinClick = { navController.navigate("pincode_setup") },
        onClearPinClick = { viewModel.clearPinCode() }
    )
}

@Composable
private fun SecuritySettingsContent(
    hasPinCode: Boolean,
    onSetPinClick: () -> Unit,
    onChangePinClick: () -> Unit,
    onClearPinClick: () -> Unit
) {
    var showConfirmClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        if (hasPinCode) {
            SettingsRow(
                text = "Изменить пин-код",
                icon = Icons.Outlined.Lock,
                onClick = onChangePinClick
            )

            HorizontalDivider(modifier = Modifier.height(1.dp))

            SettingsRow(
                text = "Удалить пин-код",
                icon = Icons.Outlined.Delete,
                color = MaterialTheme.colorScheme.error,
                onClick = { showConfirmClearDialog = true }
            )

            HorizontalDivider(modifier = Modifier.height(1.dp))

        } else {
            SettingsRow(
                text = "Установить пин-код",
                icon = Icons.Outlined.LockOpen,
                onClick = onSetPinClick
            )

            HorizontalDivider(modifier = Modifier.height(1.dp))
        }
    }

    if (showConfirmClearDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmClearDialog = false },
            title = { Text("Удалить пин-код?") },
            text = { Text("Вы уверены, что хотите удалить пин-код? При следующем входе в приложение он не будет запрашиваться.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearPinClick()
                        showConfirmClearDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmClearDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun SettingsRow(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = color
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = color
        )
    }
}