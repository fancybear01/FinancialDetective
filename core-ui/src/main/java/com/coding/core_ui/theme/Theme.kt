package com.coding.core_ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}