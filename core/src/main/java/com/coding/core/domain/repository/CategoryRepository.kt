package com.coding.core.domain.repository

import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.domain.model.categories_models.Category

/**
 * Репозиторий для работы с категориями.
 */
interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>, NetworkError>
}