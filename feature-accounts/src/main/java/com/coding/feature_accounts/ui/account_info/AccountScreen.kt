package com.coding.feature_accounts.ui.account_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core_ui.common.FullScreenError
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.LeadInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.theme.White
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.di.daggerViewModel
import com.coding.feature_accounts.di.DaggerAccountFeatureComponent

@Composable
fun AccountScreen() {

    val mainViewModel = LocalMainViewModel.current

    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()

    val updateTrigger by mainViewModel.accountUpdateTrigger.collectAsStateWithLifecycle()

    val accountId = currentAccount?.id

    if (accountId != null) {

        val context = LocalContext.current
        val accountFeatureComponent = remember {
            DaggerAccountFeatureComponent.factory()
                .create(context.appDependencies)
        }

        val accountViewModelFactory = remember(accountId) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return accountFeatureComponent
                        .accountViewModelFactory()
                        .create(accountId) as T
                }
            }
        }

        val accountViewModel: AccountViewModel = daggerViewModel(
            key = "account_$accountId",
            factory = accountViewModelFactory
        )

        LaunchedEffect(accountId, updateTrigger) {
            accountViewModel.refresh()
        }

        val state by accountViewModel.state.collectAsStateWithLifecycle()
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
        )
    )

    val currencyItem = ListItemModel(
        content = ContentInfo(
            title = "Валюта"
        ),
        trail = TrailInfo.ValueAndChevron(
            title = state.currency
        ),
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