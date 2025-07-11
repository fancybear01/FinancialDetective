package com.coding.feature_categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import com.coding.core_ui.common.list_item.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core_ui.common.FullScreenError
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.theme.DarkText
import com.coding.core_ui.theme.Gray
import com.coding.core_ui.theme.LightGray
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.model.CategoryUi
import com.coding.core_ui.model.mapper.toListItemModel

@Composable
fun CategoriesScreen() {
    val dependencies = LocalContext.current.appDependencies

    val viewModelFactory = dependencies.viewModelFactory()

    val viewModel: CategoriesViewModel = daggerViewModel(
        factory = viewModelFactory
    )

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
            CategoriesContent(
                searchQuery = state.searchQuery,
                categories = state.listItems,
                onQueryChange = viewModel::onSearchQueryChanged,
                onCategoryClick = { /* TODO() */ },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CategoriesContent(
    searchQuery: String,
    categories: List<CategoryUi>,
    onQueryChange: (String) -> Unit,
    onCategoryClick: (CategoryUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onQueryChange
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = categories,
                key = { category -> category.id }
            ) { category ->
                val model = category.toListItemModel()
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