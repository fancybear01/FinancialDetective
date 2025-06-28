package com.coding.financialdetective.features.acccount.ui

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
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.core_ui.common.FullScreenError
import com.coding.financialdetective.core_ui.common.list_item.ContentInfo
import com.coding.financialdetective.core_ui.common.list_item.LeadInfo
import com.coding.financialdetective.core_ui.common.list_item.ListItem
import com.coding.financialdetective.core_ui.common.list_item.ListItemModel
import com.coding.financialdetective.core_ui.common.list_item.TrailInfo
import com.coding.financialdetective.core_ui.theme.White
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AccountScreen() {
    val mainViewModel: MainViewModel = koinViewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()

    val accountId = currentAccount?.id

    if (accountId != null) {
        val accountViewModel: AccountViewModel = koinViewModel(
            key = "account_$accountId",
            parameters = { parametersOf(accountId.toString()) }
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
                AccountContent(
                    state = state,
                    onBalanceClick = { /* TODO: Обработка нажатия на баланс */ },
                    onCurrencyClick = { /* TODO: Обработка нажатия на валюту */ }
                )
            }
        }
    }
}

@Composable
private fun AccountContent(
    state: AccountState,
    onBalanceClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        onClick = onBalanceClick
    )

    val currencyItem = ListItemModel(
        content = ContentInfo(
            title = "Валюта"
        ),
        trail = TrailInfo.ValueAndChevron(
            title = state.currency
        ),
        onClick = onCurrencyClick
    )

    Column(modifier = modifier) {
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