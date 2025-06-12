package com.coding.financialdetective.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    text: String,
    iconStart: Int? = null,
    iconEnd: Int? = null
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    CenterAlignedTopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, // Green
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer, //DarkText,
            navigationIconContentColor = MaterialTheme.colorScheme.outline, // Gray,
            actionIconContentColor = MaterialTheme.colorScheme.outline, // Gray,
            scrolledContainerColor = MaterialTheme.colorScheme.secondary
        ),
        title = {
            Text(
                text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            iconStart?.let { icon ->
                IconButton(onClick = { TODO() }) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            iconEnd?.let { icon ->
                IconButton(onClick = { TODO() }) {
                    Icon(
                        painter = painterResource(icon), //painterResource(R.drawable.ic_history),
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}