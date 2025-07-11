package com.coding.feature_transactions.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core.domain.model.account_models.Currency
import com.coding.core_ui.common.FullScreenError
import com.coding.core_ui.common.components.CustomDatePickerDialog
import com.coding.core_ui.common.components.CustomEditFieldDialog
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.LeadInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.navigation.LocalNavController
import com.coding.core_ui.util.buttonColors
import com.coding.core_ui.util.datePickerColors
import com.coding.feature_transactions.di.DaggerTransactionFeatureComponent
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.DisposableEffect
import com.coding.core_ui.common.components.TimePickerDialog
import com.coding.core_ui.model.mapper.toListItemModel
import com.coding.core_ui.model.mapper.toUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    transactionId: Int,
    isIncome: Boolean
) {
    val mainViewModel = LocalMainViewModel.current
    val navController = LocalNavController.current

    val currentAccount by mainViewModel.currentAccount.collectAsStateWithLifecycle()
    val account = currentAccount

    if (account != null) {
        val context = LocalContext.current

        val transactionFeatureComponent = remember {
            DaggerTransactionFeatureComponent.factory()
                .create(context.appDependencies)
        }

        val viewModelKey =
            if (transactionId != -1) "transaction_detail_$transactionId" else "transaction_detail_new_$isIncome"

        val transactionDetailsViewModelFactory = remember(transactionId, isIncome) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return transactionFeatureComponent
                        .transactionDetailsViewModelFactory()
                        .create(transactionId, isIncome) as T
                }
            }
        }

        val viewModel: TransactionDetailsViewModel = daggerViewModel(
            key = viewModelKey,
            factory = transactionDetailsViewModelFactory
        )

        LaunchedEffect(key1 = Unit) {
            viewModel.setInitialAccount(account)
        }

        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = state.finishScreen) {
            if (state.finishScreen) {
                navController.popBackStack()
            }
        }

        DisposableEffect(key1 = state.isFormValid) {
            mainViewModel.setTopBarAction(enabled = state.isFormValid) {
                viewModel.saveTransaction()
            }
            onDispose {
                mainViewModel.setTopBarAction(enabled = false, action = null)
            }
        }

        val myDatePickerColors = datePickerColors()

        val myButtonColors = buttonColors()

        var showDatePicker by remember { mutableStateOf(false) }
        var showTimePicker by remember { mutableStateOf(false) }
        var showAmountDialog by remember { mutableStateOf(false) }
        var showCommentDialog by remember { mutableStateOf(false) }
        var showCategoryPicker by remember { mutableStateOf(false) }

        val categorySheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        if (showDatePicker) {
            CustomDatePickerDialog(
                initialDate = state.date,
                onDismiss = { showDatePicker = false },
                onConfirm = { selectedDate ->
                    viewModel.onDateChanged(
                        newDate = selectedDate
                    )
                },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
        }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = state.time.hour,
                initialMinute = state.time.minute,
                is24Hour = true
            )

            TimePickerDialog(
                onDismiss = { showTimePicker = false },
                onConfirm = {
                    val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    viewModel.onTimeChanged(newTime)
                    showTimePicker = false
                },
                content = {
                    TimePicker(state = timePickerState)
                }
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
                        viewModel.retry()
                    }
                )
            }

            else -> {
                TransactionsDetailContent(
                    state = state,
                    onAccountClick = { /* TODO: Показать BottomSheet для выбора счета, если у пользователя их несколько  будущем */ },
                    onCategoryClick = { showCategoryPicker = true },
                    onAmountClick = { showAmountDialog = true },
                    onDateClick = { showDatePicker = true },
                    onTimeClick = { showTimePicker = true },
                    onCommentClick = { showCommentDialog = true },
                    onDeleteClick = { viewModel.deleteTransaction() },
                    modifier = Modifier.fillMaxSize()
                )

                if (showAmountDialog) {
                    CustomEditFieldDialog(
                        title = "Сумма",
                        initialValue = state.amount.toString(),
                        keyboardType = KeyboardType.Decimal,
                        onDismiss = { showAmountDialog = false },
                        onSave = { newAmount ->
                            viewModel.onAmountChanged(newAmount)
                            showAmountDialog = false
                        }
                    )
                }

                if (showCommentDialog) {
                    CustomEditFieldDialog(
                        title = "Комментарий",
                        initialValue = state.comment,
                        keyboardType = KeyboardType.Text,
                        onDismiss = { showCommentDialog = false },
                        onSave = { newComment ->
                            viewModel.onCommentChanged(newComment)
                            showCommentDialog = false
                        }
                    )
                }

                if (showCategoryPicker) {
                    ModalBottomSheet(
                        onDismissRequest = { showCategoryPicker = false },
                        sheetState = categorySheetState
                    ) {
                        LazyColumn {
                            items(
                                items = state.availableCategories,
                                key = { category -> category.id }
                            ) { category ->
                                ListItem(
                                    model = category.toUiModel().toListItemModel(),
                                    onClick = {
                                        viewModel.onCategorySelected(category)
                                        scope.launch { categorySheetState.hide() }
                                            .invokeOnCompletion {
                                                if (!categorySheetState.isVisible) {
                                                    showCategoryPicker = false
                                                }
                                            }
                                    },
                                    modifier = Modifier.defaultMinSize(minHeight = 70.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun TransactionsDetailContent(
    state: TransactionDetailsState,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onAmountClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencySymbol = Currency.fromCode(state.currencyCode).symbol

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Счёт"),
                trail = TrailInfo.ValueAndChevron(
                    title = state.selectedAccount?.name ?: "Не выбран"
                )
            ),
            onClick = onAccountClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Статья"),
                trail = TrailInfo.ValueAndChevron(
                    title = state.selectedCategory?.name ?: "Не выбрана"
                )
            ),
            onClick = onCategoryClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Сумма"),
                trail = TrailInfo.Value(title = "${state.amount} $currencySymbol")
            ),
            onClick = onAmountClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Дата"),
                trail = TrailInfo.Value(
                    title = state.date.format(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
                    )
                )
            ),
            onClick = onDateClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Время"),
                trail = TrailInfo.Value(
                    title = state.time.format(
                        DateTimeFormatter.ofPattern(
                            "HH:mm",
                            Locale("ru")
                        )
                    )
                )
            ),
            onClick = onTimeClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(
                    title = if (state.comment.isBlank()) "Добавить комментарий" else state.comment,
                    subtitle = if (state.comment.isBlank()) null else "Нажмите, чтобы изменить"
                ),
            ),
            onClick = onCommentClick,
            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isEditing) {
            Button(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(
                        width = 380.dp,
                        height = 40.dp
                    )
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Удалить ${if (state.isIncome) "доход" else "расход"}")
            }
        }
    }
}