package com.coding.financialdetective.features.categories.data.repository

import com.coding.financialdetective.data.util.NetworkError
import com.coding.financialdetective.features.categories.data.remote.source.CategoryRemoteDataSource
import com.coding.financialdetective.features.categories.domain.model.Category
import com.coding.financialdetective.features.categories.domain.repository.CategoryRepository
import com.coding.financialdetective.data.util.Result
import com.coding.financialdetective.data.util.map
import com.coding.financialdetective.features.categories.data.mapper.toDomain

class CategoryRepositoryImpl(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<Category>, NetworkError> {
        return remoteDataSource.getCategories().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}