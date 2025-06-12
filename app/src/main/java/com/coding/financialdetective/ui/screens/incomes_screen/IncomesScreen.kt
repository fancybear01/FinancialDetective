package com.coding.financialdetective.ui.screens.incomes_screen

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.financialdetective.R
import com.coding.financialdetective.mappers.toListItemModel
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.ui.components.SummaryCard
import com.coding.financialdetective.ui.components.TopBar

@Composable
fun IncomesScreen(
    viewModel: IncomesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            SummaryCard(
                label = "Всего",
                amount = state.totalAmount
            )
        }

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