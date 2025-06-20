package com.coding.financialdetective.ui.screens.my_history_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coding.financialdetective.mappers.toListItemModel
import com.coding.financialdetective.models.ui_models.ContentInfo
import com.coding.financialdetective.models.ui_models.ListItemModel
import com.coding.financialdetective.models.ui_models.TrailInfo
import com.coding.financialdetective.ui.components.ListItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.coding.financialdetective.MainViewModel
import com.coding.financialdetective.ui.theme.DarkText
import com.coding.financialdetective.ui.theme.Gray
import com.coding.financialdetective.ui.theme.LightGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHistoryScreen(
    transactionType: String
) {
    val mainViewModel: MainViewModel = viewModel()
    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val accountId = currentAccount?.id ?: ""

    val viewModel: MyHistoryViewModel = viewModel(
        factory = MyHistoryViewModelFactory(
            accountId = accountId.toString(),
            transactionType = transactionType
        ),
        key = "history_${accountId}_$transactionType"
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateStartDate(selectedDate)
                        }
                        showStartDatePicker = false
                    },
                    colors = myButtonColors
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        text = "OK"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        text = "Отмена"
                    )
                }
            },
            colors = myDatePickerColors
        ) {
            DatePicker(
                state = datePickerState,
                colors = myDatePickerColors
            )
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.endDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateEndDate(selectedDate)
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        text = "OK"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        text = "Отмена"
                    )
                }
            },
            colors = myDatePickerColors
        ) {
            DatePicker(
                state = datePickerState,
                colors = myDatePickerColors
            )
        }
    }

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
                onClick = {
                    showStartDatePicker = true
                }
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
                onClick = {
                    showStartDatePicker = true
                }
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
                onClick = { }
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