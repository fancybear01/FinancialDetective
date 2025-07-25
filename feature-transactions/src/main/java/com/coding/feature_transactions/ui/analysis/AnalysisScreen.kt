package com.coding.feature_transactions.ui.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coding.core.domain.model.account_models.Currency
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core_ui.common.FullScreenError
import com.coding.core_ui.common.components.CustomDatePickerDialog
import com.coding.core_ui.common.list_item.ContentInfo
import com.coding.core_ui.common.list_item.ListItem
import com.coding.core_ui.common.list_item.ListItemModel
import com.coding.core_ui.common.list_item.TrailInfo
import com.coding.core_ui.di.appDependencies
import com.coding.core_ui.di.daggerViewModel
import com.coding.core_ui.navigation.LocalMainViewModel
import com.coding.core_ui.util.buttonColors
import com.coding.core_ui.util.datePickerColors
import com.coding.feature_transactions.di.DaggerTransactionFeatureComponent
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coding.core_ui.common.list_item.LeadInfo
import com.coding.feature_charts.ChartLegend
import com.coding.feature_charts.PieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(isIncome: Boolean) {
    val mainViewModel = LocalMainViewModel.current
    val account = mainViewModel.currentAccount.collectAsStateWithLifecycle().value

    if (account != null) {
        val context = LocalContext.current

        val transactionFeatureComponent = remember(account.id) {
            DaggerTransactionFeatureComponent.factory()
                .create(context.appDependencies)
        }

        val analysisViewModelFactory = remember(account.id, isIncome) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return transactionFeatureComponent
                        .analysisViewModelFactory()
                        .create(account.id, isIncome) as T
                }
            }
        }

        val viewModel: AnalysisViewModel = daggerViewModel(
            key = "analysis_${account.id}_${isIncome}",
            factory = analysisViewModelFactory
        )

        val state by viewModel.state.collectAsStateWithLifecycle()
        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        val myDatePickerColors = datePickerColors()

        val myButtonColors = buttonColors()

        if (showStartDatePicker) {
            CustomDatePickerDialog(
                initialDate = state.startDate,
                onDismiss = { showStartDatePicker = false },
                onConfirm = { selectedDate -> viewModel.updateStartDate(selectedDate) },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
        }

        if (showEndDatePicker) {
            CustomDatePickerDialog(
                initialDate = state.endDate,
                onDismiss = { showEndDatePicker = false },
                onConfirm = { selectedDate -> viewModel.updateEndDate(selectedDate) },
                colors = myDatePickerColors,
                buttonColors = myButtonColors
            )
        }



        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                FullScreenError(
                    errorMessage = state.error!!.asString(context),
                    onRetryClick = {
                        viewModel.onRefresh()
                    }
                )
            }

            else -> {
                AnalysisContent(
                    state = state,
                    onStartDateClick = { showStartDatePicker = true },
                    onEndDateClick = { showEndDatePicker = true }
                )
            }
        }
    }
}

@Composable
fun AnalysisContent(
    state: AnalysisState,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencySymbol = Currency.fromCode(state.currencyCode).symbol

    Column(modifier = modifier) {
        DateSelectorRow(
            label = "Период: начало",
            dateText = state.periodStartText,
            onClick = onStartDateClick
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        DateSelectorRow(
            label = "Период: конец",
            dateText = state.periodEndText,
            onClick = onEndDateClick
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        ListItem(
            model = ListItemModel(
                content = ContentInfo(title = "Сумма"),
                trail = TrailInfo.Value(title = "${formatNumberWithSpaces(state.totalAmount)} $currencySymbol")
            ),
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.noDataForAnalysis) {
                Text(
                    text = "Нет данных для анализа за выбранный период",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PieChart(
                                data = state.chartData,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                                    .aspectRatio(1f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            ChartLegend(
                                items = state.chartLegend,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(items = state.categoryItems, key = { it.category.id }) { item ->
                        ListItem(
                            model = ListItemModel(
                                lead = LeadInfo(
                                    emoji = item.category.emoji,
                                    containerColorForIcon = MaterialTheme.colorScheme.secondary
                                ),
                                content = ContentInfo(
                                    title = item.category.name
                                ),
                                trail = TrailInfo.ValueAndChevron(
                                    title = "%.2f%%".format(item.percentage),
                                    subtitle = "${formatNumberWithSpaces(item.totalAmount)} $currencySymbol"
                                )
                            ),
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.defaultMinSize(minHeight = 70.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSelectorRow(
    label: String,
    dateText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}