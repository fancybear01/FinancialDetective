package com.coding.core.domain.repository

import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.domain.model.categories_models.Category
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с категориями.
 */
interface CategoryRepository {

    fun getCategoriesStream(): Flow<List<Category>>

    fun getCategoriesStreamByType(isIncome: Boolean): Flow<List<Category>>

    suspend fun syncCategories(): Result<Unit, NetworkError>
}