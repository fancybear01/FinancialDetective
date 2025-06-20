package com.coding.financialdetective.ui.screens.expenses_incomes_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.mappers.toListItemModel
import com.coding.financialdetective.models.domain_models.TransactionType
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Всего"
                ),
                trail = TrailInfo.Value(
                    title = formatNumberWithSpaces(state.totalAmount) + " ₽"
                ),
                onClick = { /* ... */ }
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(
                items = state.transactions,
                key = { _, transaction -> transaction.id }
            ) { _, transaction ->
                val model = transaction.toListItemModel(
                    showDate = false
                )
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
    val mainViewModel: MainViewModel = viewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id ?: ""

    val viewModel: TransactionsViewModel = viewModel(
        factory = TransactionsViewModelFactory(
            accountId = accountId.toString(),
            transactionType = TransactionType.EXPENSE
        ),
        key = "expenses_$accountId"
    )
    TransactionsScreen(viewModel = viewModel)
}

@Composable
fun IncomesScreen() {
    val mainViewModel: MainViewModel = viewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id ?: ""

    val viewModel: TransactionsViewModel = viewModel(
        factory = TransactionsViewModelFactory(
            accountId = accountId.toString(),
            transactionType = TransactionType.INCOME
        ),
        key = "incomes_$accountId"
    )
    TransactionsScreen(viewModel = viewModel)
}