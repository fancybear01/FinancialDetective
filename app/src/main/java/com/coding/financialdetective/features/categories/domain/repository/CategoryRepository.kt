package com.coding.financialdetective.features.categories.domain.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.categories.domain.model.Category


interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>, NetworkError>
}