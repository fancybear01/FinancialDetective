package com.coding.feature_transactions.ui.expenses_incomes

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core.domain.model.account_models.Currency
import com.coding.core.domain.model.transactions_models.TransactionType
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.navigation.LocalNavController
import com.coding.core_ui.model.TransactionUi
import com.coding.core_ui.model.mapper.toListItemModel
import com.coding.feature_transactions.di.DaggerTransactionFeatureComponent

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel,
    onTransactionClick: (transactionId: String) -> Unit,
    onRetry: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
                onRetryClick = onRetry
            )
        }

        else -> {
            val currencySymbol = Currency.fromCode(state.currency).symbol

            TransactionsContent(
                totalAmount = state.totalAmount,
                transactions = state.transactions,
                currency = currencySymbol,
                onTransactionClick = onTransactionClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TransactionsContent(
    totalAmount: Double,
    transactions: List<TransactionUi>,
    currency: String,
    onTransactionClick: (transactionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Всего"
                ),
                trail = TrailInfo.Value(
                    title = formatNumberWithSpaces(totalAmount) + " $currency"
                )
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = transactions,
                key = { transaction -> transaction.id }
            ) { transaction ->
                val model = transaction.toListItemModel(showDate = false)
                ListItem(
                    model = model,
                    onClick = { onTransactionClick(transaction.id) },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 70.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Composable
fun ExpensesScreen() {
    val mainViewModel = LocalMainViewModel.current
    val navController = LocalNavController.current
    val context = LocalContext.current

    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val account = currentAccount

    if (account != null) {
        val transactionFeatureComponent = remember {
            DaggerTransactionFeatureComponent.factory().create(context.appDependencies)
        }
        val transactionsViewModelFactory = remember(account.id) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return transactionFeatureComponent.transactionsViewModelFactory()
                        .create(account.id, TransactionType.EXPENSE) as T
                }
            }
        }
        val viewModel: TransactionsViewModel = daggerViewModel(
            key = "expenses_${account.id}",
            factory = transactionsViewModelFactory
        )

        LaunchedEffect(key1 = account) {
            viewModel.updateAccount(account)
        }

        TransactionsScreen(
            viewModel = viewModel,
            onTransactionClick = { transactionId ->
                Log.d("NAV_DEBUG", "Navigating with transactionId: '$transactionId'")
                navController.navigate("expense_details?transactionId=$transactionId")
            },
            onRetry = {
                viewModel.onRefresh()
            }
        )
    }
}

@Composable
fun IncomesScreen() {
    val mainViewModel = LocalMainViewModel.current
    val navController = LocalNavController.current
    val context = LocalContext.current

    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val account = currentAccount

    if (account != null) {
        val transactionFeatureComponent = remember {
            DaggerTransactionFeatureComponent.factory().create(context.appDependencies)
        }
        val transactionsViewModelFactory = remember(account.id) {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return transactionFeatureComponent.transactionsViewModelFactory()
                        .create(account.id, TransactionType.INCOME) as T
                }
            }
        }
        val viewModel: TransactionsViewModel = daggerViewModel(
            key = "incomes_${account.id}",
            factory = transactionsViewModelFactory
        )

        LaunchedEffect(key1 = account) {
            viewModel.updateAccount(account)
        }

        TransactionsScreen(
            viewModel = viewModel,
            onTransactionClick = { transactionId ->
                navController.navigate("income_details?transactionId=$transactionId")
            },
            onRetry = { viewModel.onRefresh() }
        )
    }
}