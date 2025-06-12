package com.coding.financialdetective.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = White,
    secondary = DarkMintGreen,
    onPrimaryContainer = White,
    onSecondaryContainer = DarkText,
    surface = DarkGrayBackground,
    onSurface = White,
    onSurfaceVariant = NavigationBar,
    outlineVariant = LightGrayBorder,
    outline = DarkGrayBorder
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
    outline = Gray
)

@Composable
fun FinancialDetectiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}