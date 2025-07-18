package com.coding.feature_transactions.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core.domain.model.account_models.AccountBrief
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core_ui.util.toUiText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TransactionDetailsViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    @Assisted private val transactionId: String?,
    @Assisted private val isIncome: Boolean
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(transactionId: String?, isIncome: Boolean): TransactionDetailsViewModel
    }

    private val _state = MutableStateFlow(TransactionDetailsState())
    val state: StateFlow<TransactionDetailsState> = _state.asStateFlow()

    init {
        Log.d("ViewModel_DEBUG", "ViewModel created with transactionId: '$transactionId'")

        _state.update { it.copy(isIncome = isIncome, isEditing = transactionId != null) }

        if (transactionId != null) {
            loadTransactionDetails()
        } else {
            observeAvailableCategories()
            syncCategories()
        }
    }

    private fun loadTransactionDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result: Result<Transaction, NetworkError>
            if (transactionId!!.startsWith("local_")) {
                val localId = transactionId.removePrefix("local_").toLongOrNull()
                result = if (localId != null) {
                    transactionRepository.getLocalTransactionById(localId)
                } else {
                    Result.Error(NetworkError.BAD_REQUEST)
                }
            } else {
                val remoteId = transactionId.toIntOrNull()
                result = if (remoteId != null) {
                    transactionRepository.getTransactionById(remoteId)
                } else {
                    Result.Error(NetworkError.BAD_REQUEST)
                }
            }
            result.onSuccess { transaction ->
                _state.update {
                    val isTransactionSynced = !transaction.id.startsWith("local_")
                    it.copy(
                        transactionId = transaction.id,
                        selectedAccount = transaction.account,
                        selectedCategory = transaction.category,
                        amount = transaction.amount,
                        date = transaction.transactionDate.toLocalDate(),
                        time = transaction.transactionDate.toLocalTime(),
                        comment = transaction.comment,
                        currencyCode = transaction.account.currency,
                        isLoading = false,
                        isIncome = isTransactionSynced
                    )
                }
                observeAvailableCategories()
                syncCategories()
                validateForm()
            }.onError { networkError ->
                _state.update { it.copy(isLoading = false, error = networkError.toUiText()) }
            }
        }
    }

    private fun observeAvailableCategories() {
        categoryRepository.getCategoriesStreamByType(isIncome)
            .onEach { categories ->
                _state.update { it.copy(availableCategories = categories) }
                if (!state.value.isEditing && state.value.selectedCategory == null && categories.isNotEmpty()) {
                    _state.update { it.copy(selectedCategory = categories.first()) }
                    validateForm()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun syncCategories() {
        viewModelScope.launch {
            categoryRepository.syncCategories()
        }
    }

    fun setInitialAccount(account: AccountBrief) {
        if (!state.value.isEditing && state.value.selectedAccount == null) {
            _state.update {
                it.copy(
                    selectedAccount = account,
                    currencyCode = account.currency
                )
            }
            validateForm()
        }
    }

    fun onCategorySelected(category: Category) {
        _state.update { it.copy(selectedCategory = category) }
        validateForm()
    }

    fun onAmountChanged(amountStr: String) {
        val cleanedAmount = amountStr.replace(",", ".").toDoubleOrNull() ?: 0.0
        _state.update { it.copy(amount = cleanedAmount) }
        validateForm()
    }

    fun onDateChanged(newDate: LocalDate) {
        _state.update { it.copy(date = newDate) }
        validateForm()
    }

    fun onTimeChanged(newTime: LocalTime) {
        _state.update { it.copy(time = newTime) }
        validateForm()
    }

    fun onCommentChanged(newComment: String) {
        _state.update { it.copy(comment = newComment) }
        validateForm()
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val currentState = state.value

            if (currentState.selectedAccount == null || currentState.selectedCategory == null) {
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val transactionDate =
                ZonedDateTime.of(currentState.date, currentState.time, ZoneOffset.UTC)

            val result = if (currentState.isEditing) {
                transactionRepository.updateTransaction(
                    id = currentState.transactionId!!,
                    accountId = currentState.selectedAccount.id.toInt(),
                    categoryId = currentState.selectedCategory.id,
                    transactionDate = transactionDate,
                    amount = currentState.amount,
                    comment = currentState.comment
                )

            } else {
                transactionRepository.createTransaction(
                    accountId = currentState.selectedAccount.id.toInt(),
                    categoryId = currentState.selectedCategory.id,
                    transactionDate = transactionDate,
                    amount = currentState.amount,
                    comment = currentState.comment
                )
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, finishScreen = true) }
            }.onError { networkError ->
                _state.update { it.copy(isLoading = false, error = networkError.toUiText()) }
            }
        }
    }

    fun deleteTransaction() {
        val transactionIdToDelete = state.value.transactionId
        if (transactionIdToDelete == null || !state.value.isEditing) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            transactionRepository.deleteTransaction(transactionIdToDelete)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, finishScreen = true) }
                }.onError { networkError ->
                    _state.update { it.copy(isLoading = false, error = networkError.toUiText()) }
                }
        }
    }

    private fun validateForm() {
        val currentState = _state.value
        val isValid = currentState.selectedAccount != null &&
                currentState.selectedCategory != null &&
                currentState.amount > 0
        _state.update { it.copy(isFormValid = isValid) }
    }

    fun retry() {
        if (transactionId != null) {
            loadTransactionDetails()
        }
        syncCategories()
    }
}