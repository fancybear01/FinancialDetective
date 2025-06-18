package com.coding.financialdetective.ui.screens.expenses_screen

import androidx.compose.foundation.background
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
import com.coding.financialdetective.mappers.toListItemModel
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Всего"
                ),
                trail = TrailInfo.Value(
                    title = formatNumberWithSpaces(state.totalAmount) + " ₽"
                ),
                onClick = { TODO() }
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
                items = state.expenses,
                key = { _, expense -> expense.id }
            ) { _, expense ->
                val model = expense.toListItemModel()
                ListItem(
                    model = model,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 72.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}