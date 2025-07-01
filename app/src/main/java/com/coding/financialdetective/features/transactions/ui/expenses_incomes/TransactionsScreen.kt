package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.coding.financialdetective.core_ui.common.list_item.ListItem
import com.coding.financialdetective.core_ui.common.list_item.ListItemModel
import com.coding.financialdetective.core_ui.common.list_item.TrailInfo
import com.coding.financialdetective.core_ui.common.list_item.toListItemModel
import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.features.transactions.domain.model.TransactionType
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel
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
                onRetryClick = { viewModel.retry() }
            )
        }

        else -> {
            TransactionsContent(
                totalAmount = state.totalAmount,
                transactions = state.transactions,
                onTotalClick = {
                    // TODO()
                },
                onTransactionClick = { transaction ->
                    // TODO()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TransactionsContent(
    totalAmount: Double,
    transactions: List<TransactionUi>,
    onTotalClick: () -> Unit,
    onTransactionClick: (TransactionUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Всего"
                ),
                trail = TrailInfo.Value(
                    title = formatNumberWithSpaces(totalAmount) + " ₽"
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
    val mainViewModel: MainViewModel = koinViewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id

    if (accountId != null) {
        val viewModel: TransactionsViewModel = koinViewModel(
            key = "expenses_$accountId"
        ) {
            parametersOf(accountId.toString(), TransactionType.EXPENSE)
        }

        TransactionsScreen(viewModel = viewModel)
    }
}

@Composable
fun IncomesScreen() {
    val mainViewModel: MainViewModel = koinViewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id

    if (accountId != null) {
        val viewModel: TransactionsViewModel = koinViewModel(
            key = "incomes_$accountId"
        ) {
            parametersOf(accountId.toString(), TransactionType.INCOME)
        }

        TransactionsScreen(viewModel = viewModel)
    }
}