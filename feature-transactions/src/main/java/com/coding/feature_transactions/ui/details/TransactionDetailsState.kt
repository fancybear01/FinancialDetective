package com.coding.feature_transactions.ui.details

import com.coding.core.domain.model.account_models.AccountBrief
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.util.UiText
import java.time.LocalDate
import java.time.LocalTime

data class TransactionDetailsState(
    val transactionId: String? = null,
    val selectedAccount: AccountBrief? = null,
    val selectedCategory: Category? = null,
    val amount: Double = 0.0,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val comment: String = "",
    val currencyCode: String = "",
    val isIncome: Boolean = false,

    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val availableCategories: List<Category> = emptyList(),
    val finishScreen: Boolean = false,
    val isFormValid: Boolean = false,
    val isSynced: Boolean = false
)