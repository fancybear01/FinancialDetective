package com.coding.core_ui.common.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.coding.core_ui.navigation.currentRouteAsState
import com.coding.core_ui.navigation.getScreen
import com.coding.core_ui.navigation.screens

@Composable
fun AppFloatingActionButton(navController: NavController) {
    val currentDestination = navController.currentRouteAsState()

    val currentScreen = getScreen(currentDestination)

    val action = currentScreen.action

    if (currentScreen in screens && action != null) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier.clip(CircleShape),
            onClick = {}
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}