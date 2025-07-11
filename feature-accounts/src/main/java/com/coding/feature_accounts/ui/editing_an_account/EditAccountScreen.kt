package com.coding.feature_accounts.ui.editing_an_account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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
import com.coding.core.domain.model.account_models.Currency
import com.coding.core_ui.common.components.CustomEditFieldDialog
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.di.daggerViewModel
import com.coding.feature_accounts.R
import com.coding.feature_accounts.di.DaggerAccountFeatureComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen() {
    val mainViewModel = LocalMainViewModel.current

    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id

    if (accountId != null) {
        val context = LocalContext.current

        val accountFeatureComponent = remember {
            DaggerAccountFeatureComponent.factory()
                .create(context.appDependencies)
        }

        val editAccountViewModelFactory = remember(accountId) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return accountFeatureComponent
                        .editAccountViewModelFactory()
                        .create(accountId) as T
                }
            }
        }

        val viewModel: EditAccountViewModel = daggerViewModel(
            key = "edit_account_$accountId",
            factory = editAccountViewModelFactory
        )

        val state by viewModel.state.collectAsStateWithLifecycle()

        val scope = rememberCoroutineScope()

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showCurrencyPicker by remember { mutableStateOf(false) }
        var showNameDialog by remember { mutableStateOf(false) }
        var showBalanceDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.saveSuccessEvent.collect {
                mainViewModel.onAccountManuallyUpdated(
                    accountId = accountId,
                    newName = state.accountName,
                    newBalance = state.rawBalance,
                    newCurrencyCode = state.selectedCurrency.code
                )
                mainViewModel.navigateBack()
            }
        }

        DisposableEffect(state.hasChanges) {
            if (state.hasChanges) {
                mainViewModel.setTopBarAction(enabled = true) {
                    scope.launch {
                        viewModel.saveChanges()
                    }
                }
            } else {
                mainViewModel.setTopBarAction(enabled = false, action = null)
            }

            onDispose {
                mainViewModel.setTopBarAction(enabled = false, action = null)
            }
        }

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                FullScreenError(
                    errorMessage = state.error!!.asString(context),
                    onRetryClick = viewModel::retry
                )
            }

            else -> {
                EditAccountContent(
                    state = state,
                    onAccountNameClick = { showNameDialog = true },
                    onAccountBalanceClick = { showBalanceDialog = true },
                    onAccountCurrencySymbolClick = { showCurrencyPicker = true }
                )

                if (showNameDialog) {
                    CustomEditFieldDialog(
                        title = "Название счета",
                        initialValue = state.accountName,
                        onDismiss = { showNameDialog = false },
                        onSave = { newName ->
                            viewModel.onAccountNameChanged(newName)
                            showNameDialog = false
                        }
                    )
                }
                if (showBalanceDialog) {
                    CustomEditFieldDialog(
                        title = "Баланс",
                        initialValue = state.balance,
                        keyboardType = KeyboardType.Decimal,
                        onDismiss = { showBalanceDialog = false },
                        onSave = { newBalance ->
                            viewModel.onBalanceChanged(newBalance)
                            showBalanceDialog = false
                        }
                    )
                }
                if (showCurrencyPicker) {
                    ModalBottomSheet(
                        onDismissRequest = { showCurrencyPicker = false },
                        sheetState = sheetState
                    ) {
                        CurrencyBottomSheet(
                            currencies = Currency.values().toList(),
                            onCurrencySelected = { currency ->
                                viewModel.onCurrencySelected(currency)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showCurrencyPicker = false
                                    }
                                }
                            },
                            onCancel = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showCurrencyPicker = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EditAccountContent(
    state: EditAccountState,
    onAccountNameClick: () -> Unit,
    onAccountBalanceClick: () -> Unit,
    onAccountCurrencySymbolClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            model = ListItemModel(
                lead = LeadInfo(
                    emoji = "💰",
                    containerColorForIcon = MaterialTheme.colorScheme.secondary
                ),
                content = ContentInfo(
                    title = "Название"
                ),
                trail = TrailInfo.Value(
                    title = state.accountName
                ),
            ),
            modifier = Modifier
                .defaultMinSize(minHeight = 70.dp),
            onClick = onAccountNameClick
        )

        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Баланс"
                ),
                trail = TrailInfo.Value(
                    title = state.balance
                ),
            ),
            modifier = Modifier
                .defaultMinSize(minHeight = 70.dp),
            onClick = onAccountBalanceClick
        )

        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Валюта"
                ),
                trail = TrailInfo.Value(
                    title = state.currencySymbol
                ),
            ),
            modifier = Modifier
                .defaultMinSize(minHeight = 70.dp),
            onClick = onAccountCurrencySymbolClick
        )

    }
}

@Composable
fun CurrencyBottomSheet(
    currencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        currencies.forEach { currency ->
            ListItem(
                model = ListItemModel(
                    content = ContentInfo(
                        title = "${currency.displayName} ${currency.symbol}"
                    )
                ),
                onClick = { onCurrencySelected(currency) },
                modifier = Modifier
                    .clickable { onCurrencySelected(currency) }
                    .defaultMinSize(minHeight = 70.dp)
            )
        }

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_cancel),
                    contentDescription = "Отмена"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Отмена",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}