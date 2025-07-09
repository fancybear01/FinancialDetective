package com.coding.core_ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMainViewModel = staticCompositionLocalOf<MainViewModelContract> {
    error("No MainViewModelContract provided")
}