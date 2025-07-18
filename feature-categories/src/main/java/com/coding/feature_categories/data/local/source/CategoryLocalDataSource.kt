package com.coding.feature_categories.data.local.source

import com.coding.core.data.local.dao.CategoryDao
import com.coding.core.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getCategoriesStream(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    fun getCategoriesStreamByType(isIncome: Boolean): Flow<List<CategoryEntity>> {
        val type = if (isIncome) "INCOME" else "EXPENSE"
        return categoryDao.getCategoriesByType(type)
    }

    suspend fun upsertCategories(categories: List<CategoryEntity>) {
        categoryDao.upsertAll(categories)
    }
}