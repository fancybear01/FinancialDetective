package com.coding.financialdetective.core.domain.repositories

import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.models.domain_models.Category

interface CategoriesDataSource {
    suspend fun getCategories(): Result<List<Category>, NetworkError>
}