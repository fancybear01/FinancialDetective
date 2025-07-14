package com.coding.feature_categories.data.remote.source

import com.coding.core.data.remote.service.constructUrl
import com.coding.core.data.remote.service.safeCallWithRetry
import com.coding.core.data.util.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.coding.core.data.util.Result
import com.coding.core.data.remote.dto.categoties_dto.CategoryDto
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getCategories(): Result<List<CategoryDto>, NetworkError> {
        return safeCallWithRetry<List<CategoryDto>> {
            httpClient.get(constructUrl("categories"))
        }
    }

    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDto>, NetworkError> {
        return safeCallWithRetry {
            httpClient.get(constructUrl("categories/type/$isIncome"))
        }
    }
}