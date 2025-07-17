package com.coding.feature_transactions.ui.my_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.coding.core_ui.common.FullScreenError
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coding.core.domain.model.account_models.Currency
import com.coding.core_ui.common.components.CustomDatePickerDialog
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.navigation.LocalNavController
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.util.buttonColors
import com.coding.core_ui.util.datePickerColors
import com.coding.feature_transactions.di.DaggerTransactionFeatureComponent
import com.coding.core_ui.model.mapper.toListItemModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHistoryScreen() {
    val mainViewModel = LocalMainViewModel.current

    val account = mainViewModel.currentAccount.collectAsStateWithLifecycle().value

    val navController = LocalNavController.current
    val navBackStackEntry = navController.currentBackStackEntry
    val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome") ?: false

    if (account != null) {
        val context = LocalContext.current

        val transactionFeatureComponent = remember(account.id) {
            DaggerTransactionFeatureComponent.factory()
                .create(context.appDependencies)
        }

        val myHistoryViewModelFactory = remember(account.id, isIncome) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return transactionFeatureComponent
                        .myHistoryViewModelFactory()
                        .create(account.id, isIncome) as T
                }
            }
        }

        val viewModel: MyHistoryViewModel = daggerViewModel(
            key = "my_history_${account.id}_${isIncome}",
            factory = myHistoryViewModelFactory
        )

        val state by viewModel.state.collectAsStateWithLifecycle()
        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        val myDatePickerColors = datePickerColors()

        val myButtonColors = buttonColors()

        if (showStartDatePicker) {
            CustomDatePickerDialog(
                initialDate = state.startDate,
                onDismiss = { showStartDatePicker = false },
                onConfirm = { selectedDate -> viewModel.updateStartDate(selectedDate) },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
        }

        if (showEndDatePicker) {
            CustomDatePickerDialog(
                initialDate = state.endDate,
                onDismiss = { showEndDatePicker = false },
                onConfirm = { selectedDate -> viewModel.updateEndDate(selectedDate) },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
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
                    onRetryClick = {
                        viewModel.onRefresh()
                    }
                )
            }

            else -> {
                MyHistoryContent(
                    state = state,
                    onStartDateClick = { showStartDatePicker = true },
                    onEndDateClick = { showEndDatePicker = true },
                    onTransactionClick = { transactionId ->
                        val route = if (isIncome) "income_details" else "expense_details"
                        navController.navigate("$route?transactionId=$transactionId")
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MyHistoryContent(
    state: MyHistoryState,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onTransactionClick: (transactionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencySymbol = Currency.fromCode(state.currencyCode).symbol

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Начало"),
                trail = TrailInfo.Value(title = state.periodStart)
            ),
            onClick = onStartDateClick,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Конец"),
                trail = TrailInfo.Value(title = state.periodEnd)
            ),
            onClick = onEndDateClick,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Сумма"),
                trail = TrailInfo.Value(title = "${state.totalAmount} $currencySymbol")
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp),
            addDivider = false
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = state.listItems,
                key = { item -> item.id }
            ) { transaction ->
                ListItem(
                    model = transaction.toListItemModel(),
                    onClick = { onTransactionClick(transaction.id) },
                    modifier = Modifier.defaultMinSize(minHeight = 70.dp)
                )
            }
        }
    }
}