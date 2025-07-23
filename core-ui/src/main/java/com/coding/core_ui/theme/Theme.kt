package com.coding.core_ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core_ui.util.getAppColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = DarkText,
    secondary = DarkMintGreen,
    onSecondary = White,
    onPrimaryContainer = LightText,
    onSecondaryContainer = LightText,
    surface = DarkGrayBackground,
    onSurface = LightText,
    onSurfaceVariant = LightGray,
    outlineVariant = DarkGrayBorder,
    outline = Gray,
    error = Cancel
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = White,
    secondary = MintGreen,
    onPrimaryContainer = DarkText,
    onSecondaryContainer = DarkText,
    surface = Surface,
    onSurface = DarkText,
    onSurfaceVariant = NavigationBar,
    outlineVariant = LightGrayBorder,
    outline = Gray,
    error = Cancel
)

@Composable
fun FinancialDetectiveTheme(
    darkTheme: Boolean,
    colorSchemeSetting: ColorSchemeSetting,
    content: @Composable () -> Unit
) {
    val colorScheme = getAppColorScheme(scheme = colorSchemeSetting, isDark = darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}