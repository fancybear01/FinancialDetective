package com.coding.financialdetective.core.data.remote

import com.coding.financialdetective.core.domain.repositories.CategoriesDataSource
import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.core.networking.constructUrl
import com.coding.financialdetective.models.data_models.CategoryDto
import com.coding.financialdetective.models.domain_models.Category
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.coding.financialdetective.core.domain.util.map
import com.coding.financialdetective.core.networking.safeCallWithRetry
import com.coding.financialdetective.mappers.toDomain

class RemoteCategoriesDataSource(
    private val httpClient: HttpClient
) : CategoriesDataSource {
    override suspend fun getCategories(): Result<List<Category>, NetworkError> {
        return safeCallWithRetry<List<CategoryDto>> {
            httpClient.get(constructUrl("categories"))
        }.map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}