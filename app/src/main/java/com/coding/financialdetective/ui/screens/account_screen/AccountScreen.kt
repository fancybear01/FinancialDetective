package com.coding.financialdetective.ui.screens.account_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.LeadInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.ui.theme.White

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val balanceItem = ListItemModel(
        lead = LeadInfo(
            emoji = "💰",
            containerColor = White
        ),
        content = ContentInfo(
            title = "Баланс"
        ),
        trail = TrailInfo.ValueAndChevron(
            title = "-670 000 ₽"
        ),
        onClick = { TODO() }
    )

    val currencyItem = ListItemModel(
        content = ContentInfo(
            title = "Валюта"
        ),
        trail = TrailInfo.ValueAndChevron(
            title = "₽"
        ),
        onClick = { TODO() }
    )

    Column {
        ListItem(
            model = balanceItem,
            containerColorForIcon = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )
        ListItem(
            model = currencyItem,
            containerColorForIcon = MaterialTheme.colorScheme.secondary,
            addDivider = false,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )
    }
}