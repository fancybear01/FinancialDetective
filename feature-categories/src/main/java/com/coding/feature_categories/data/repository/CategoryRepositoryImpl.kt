package com.coding.feature_categories.data.repository

import com.coding.core.data.util.NetworkError
import com.coding.feature_categories.data.remote.source.CategoryRemoteDataSource
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.core.data.mapper.toEntity
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onSuccess
import com.coding.feature_categories.data.local.source.CategoryLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
    private val connectivityObserver: ConnectivityObserver
) : CategoryRepository {

    override fun getCategoriesStream(): Flow<List<Category>> {
        return localDataSource.getCategoriesStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCategoriesStreamByType(isIncome: Boolean): Flow<List<Category>> {
        return localDataSource.getCategoriesStreamByType(isIncome).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncCategories(): Result<Unit, NetworkError> {
        if (!connectivityObserver.isConnected.first()) {
            return Result.Success(Unit)
        }

        return remoteDataSource.getCategories().onSuccess { dtoList ->
            val entities = dtoList.map { it.toEntity() }
            localDataSource.upsertCategories(entities)
        }.map { }
    }
}