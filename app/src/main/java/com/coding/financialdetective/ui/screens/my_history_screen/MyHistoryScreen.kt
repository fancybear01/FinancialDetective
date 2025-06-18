package com.coding.financialdetective.ui.screens.my_history_screen

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

@Composable
fun MyHistoryScreen(
    viewModel: MyHistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Начало"
                ),
                trail = TrailInfo.Value(
                    title = state.periodStart
                ),
                onClick = { TODO() }
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Конец"
                ),
                trail = TrailInfo.Value(
                    title = state.periodEnd
                ),
                onClick = { TODO() }
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = "Сумма"
                ),
                trail = TrailInfo.Value(
                    title = state.totalAmount
                ),
                onClick = { TODO() }
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp),
            addDivider = false
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(
                items = state.listItems,
                key = { _, item -> item.id }
            ) { _, transaction ->
                ListItem(
                    model = transaction.toListItemModel(),
                    modifier = Modifier
                        .defaultMinSize(minHeight = 70.dp)
                )
            }
        }
    }
}