package com.coding.financialdetective.features.categories.data.remote.source

import com.coding.financialdetective.data.remote.service.constructUrl
import com.coding.financialdetective.data.remote.service.safeCallWithRetry
import com.coding.financialdetective.data.util.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.features.categories.data.remote.dto.CategoryDto
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getCategories(): Result<List<CategoryDto>, NetworkError> {
        return safeCallWithRetry<List<CategoryDto>> {
            httpClient.get(constructUrl("categories"))
        }
    }
}