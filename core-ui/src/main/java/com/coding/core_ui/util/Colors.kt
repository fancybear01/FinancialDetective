package com.coding.core_ui.util

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core_ui.theme.BluePrimary
import com.coding.core_ui.theme.BlueSecondary
import com.coding.core_ui.theme.GreenPrimary
import com.coding.core_ui.theme.GreenSecondary
import com.coding.core_ui.theme.OrangePrimary
import com.coding.core_ui.theme.OrangeSecondary
import com.coding.core_ui.theme.PurplePrimary
import com.coding.core_ui.theme.PurpleSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerColors(): DatePickerColors {
    return DatePickerDefaults.colors(
        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
        selectedDayContentColor = MaterialTheme.colorScheme.onSurface,
        todayDateBorderColor = MaterialTheme.colorScheme.secondary,
        todayContentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = MaterialTheme.colorScheme.outline,
        headlineContentColor = MaterialTheme.colorScheme.outline,
        navigationContentColor = MaterialTheme.colorScheme.outline,
        dividerColor = MaterialTheme.colorScheme.secondary,
        subheadContentColor = MaterialTheme.colorScheme.outline,
        dateTextFieldColors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,

            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,

            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),

            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,

            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun buttonColors(): ButtonColors {
    return ButtonDefaults.textButtonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun getAppColorScheme(
    scheme: ColorSchemeSetting,
    isDark: Boolean
): ColorScheme {
    val baseScheme = if (isDark) darkColorScheme() else lightColorScheme()

    return when (scheme) {
        ColorSchemeSetting.GREEN -> baseScheme.copy(primary = GreenPrimary, secondary = GreenSecondary)
        ColorSchemeSetting.BLUE -> baseScheme.copy(primary = BluePrimary, secondary = BlueSecondary)
        ColorSchemeSetting.ORANGE -> baseScheme.copy(primary = OrangePrimary, secondary = OrangeSecondary)
        ColorSchemeSetting.PURPLE -> baseScheme.copy(primary = PurplePrimary, secondary = PurpleSecondary)
    }
}