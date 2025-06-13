package com.coding.financialdetective.ui.screens.incomes_screen

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
import com.coding.financialdetective.models.domain_models.ContentInfo
import com.coding.financialdetective.models.domain_models.ListItemModel
import com.coding.financialdetective.models.domain_models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.utils.formatNumberWithSpaces

@Composable
fun IncomesScreen(
    viewModel: IncomesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Всего"
                ),
                trail = TrailInfo.Value(
                    value = formatNumberWithSpaces(state.totalAmount) + " ₽"
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
                items = state.incomes,
                key = { _, income -> income.id }
            ) { _, income ->
                val model = income.toListItemModel()
                ListItem(
                    model = model,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 72.dp)
                )
            }
        }
    }
}