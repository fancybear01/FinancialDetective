package com.coding.financialdetective.ui.screens.account_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.LeadInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.components.FullScreenError
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.ui.theme.White

@Composable
fun AccountScreen() {
    val mainViewModel: MainViewModel = viewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id ?: ""

    val accountViewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accountId.toString()),
        key = "account_$accountId"
    )

    val state by accountViewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val currentError = state.error

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        currentError != null -> {
            FullScreenError(
                errorMessage = currentError.asString(context),
                onRetryClick = { accountViewModel.retry() }
            )
        }

        else -> {
            val balanceItem = ListItemModel(
                lead = LeadInfo(
                    emoji = "💰",
                    containerColorForIcon = White
                ),
                content = ContentInfo(
                    title = "Баланс"
                ),
                trail = TrailInfo.ValueAndChevron(
                    title = "${state.balance} ${state.currency}"
                ),
                onClick = { /* TODO() */ }
            )

            val currencyItem = ListItemModel(
                content = ContentInfo(
                    title = "Валюта"
                ),
                trail = TrailInfo.ValueAndChevron(
                    title = state.currency
                ),
                onClick = { /* TODO() */ }
            )

            Column {
                ListItem(
                    model = balanceItem,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 56.dp)
                )
                ListItem(
                    model = currencyItem,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    addDivider = false,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 56.dp)
                )
            }
        }
    }
}