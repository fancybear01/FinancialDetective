package com.coding.financialdetective.features.categories.di

import com.coding.financialdetective.features.categories.data.remote.source.CategoryRemoteDataSource
import com.coding.financialdetective.features.categories.data.repository.CategoryRepositoryImpl
import com.coding.financialdetective.features.categories.domain.repository.CategoryRepository
import com.coding.financialdetective.features.categories.ui.CategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    viewModel {
        CategoriesViewModel(
            repository = get(),
            connectivityObserver = get()
        )
    }

    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    factory { CategoryRemoteDataSource(get()) }
}