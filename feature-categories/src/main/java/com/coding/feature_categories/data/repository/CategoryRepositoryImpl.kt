package com.coding.feature_categories.data.repository

import com.coding.core.data.util.NetworkError
import com.coding.feature_categories.data.remote.source.CategoryRemoteDataSource
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<Category>, NetworkError> {
        return remoteDataSource.getCategories().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<Category>, NetworkError> {
        return remoteDataSource.getCategoriesByType(isIncome).map { it.map { dto -> dto.toDomain() } }
    }
}