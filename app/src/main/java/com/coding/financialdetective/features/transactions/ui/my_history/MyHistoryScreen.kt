package com.coding.financialdetective.features.transactions.ui.my_history

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.core_ui.common.FullScreenError
import com.coding.financialdetective.core_ui.common.list_item.ContentInfo
import com.coding.financialdetective.core_ui.common.list_item.ListItem
import com.coding.financialdetective.core_ui.common.list_item.ListItemModel
import com.coding.financialdetective.core_ui.common.list_item.TrailInfo
import com.coding.financialdetective.core_ui.common.list_item.toListItemModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePickerColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.coding.financialdetective.features.acccount.domain.model.Currency
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHistoryScreen() {
    val mainViewModel: MainViewModel = koinViewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val account = currentAccount

    if (account != null) {
        val viewModel: MyHistoryViewModel = koinViewModel(key = "my_history_${account.id}") {
            parametersOf(account.id)
        }

        LaunchedEffect(key1 = account.id, key2 = account.currency) {
            viewModel.onAccountUpdated(account.currency)
        }

        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current

        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        val myDatePickerColors = DatePickerDefaults.colors(
            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
            selectedDayContentColor = MaterialTheme.colorScheme.onSurface,
            todayDateBorderColor = MaterialTheme.colorScheme.secondary,
            todayContentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.outline,
            headlineContentColor = MaterialTheme.colorScheme.outline,
            navigationContentColor = MaterialTheme.colorScheme.outline,
            dividerColor = MaterialTheme.colorScheme.secondary,
            subheadContentColor = MaterialTheme.colorScheme.outline,
            dateTextFieldColors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error,

                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,

                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),

                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,

                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        val myButtonColors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )

        if (showStartDatePicker) {
            MyHistoryDatePickerDialog(
                initialDate = state.startDate,
                onDismiss = { showStartDatePicker = false },
                onConfirm = { selectedDate -> viewModel.updateStartDate(selectedDate) },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
        }

        if (showEndDatePicker) {
            MyHistoryDatePickerDialog(
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
                        val newCurrencyCode = currentAccount?.currency ?: ""
                        viewModel.retry(newCurrencyCode)
                    }
                )
            }
            else -> {
                MyHistoryContent(
                    state = state,
                    onStartDateClick = { showStartDatePicker = true },
                    onEndDateClick = { showEndDatePicker = true },
                    onTransactionClick = { /* TODO() */ },
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
    onTransactionClick: (TransactionUi) -> Unit,
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
            ) { item ->
                ListItem(
                    model = item.toListItemModel(),
                    modifier = Modifier.defaultMinSize(minHeight = 70.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyHistoryDatePickerDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    colors: DatePickerColors,
    buttonColors: ButtonColors
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onConfirm(selectedDate)
                    }
                    onDismiss()
                },
                colors = buttonColors
            ) {
                Text(text = "OK", style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = buttonColors) {
                Text(text = "Отмена", style = MaterialTheme.typography.labelLarge)
            }
        },
        colors = colors
    ) {
        DatePicker(state = datePickerState, colors = colors)
    }
}