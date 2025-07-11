package com.coding.core_ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
    outline = Gray,
    error = Cancel
)

@Composable
fun FinancialDetectiveTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}