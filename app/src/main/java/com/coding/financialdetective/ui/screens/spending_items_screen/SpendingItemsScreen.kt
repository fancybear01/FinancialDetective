package com.coding.financialdetective.ui.screens.spending_items_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.financialdetective.mappers.toListItemModel
import com.coding.financialdetective.ui.components.ListItem
import com.coding.financialdetective.ui.components.TopBar
import com.coding.financialdetective.ui.theme.DarkText
import com.coding.financialdetective.ui.theme.Gray
import com.coding.financialdetective.ui.theme.LightGray

@Composable
fun SpendingItemsScreen(
    viewModel: SpendingItemsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Column {

        SearchBar(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
            }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            itemsIndexed(
                items = state.results,
                key = { _, result -> result.id }
            ) { _, result ->
                val model = result.toListItemModel()
                ListItem(
                    model = model,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 72.dp)
                )
            }

        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        placeholder = {
            Text(text = "Найти статью")
        },
        leadingIcon = null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            errorIndicatorColor = Color.Transparent,

            focusedContainerColor = LightGray,
            unfocusedContainerColor = LightGray,

            focusedPlaceholderColor = Gray,
            unfocusedPlaceholderColor = Gray,

            focusedTextColor = DarkText,
            unfocusedTextColor = DarkText,

            focusedTrailingIconColor = Gray,
            unfocusedTrailingIconColor = Gray,

            cursorColor = Gray
        )
    )
}