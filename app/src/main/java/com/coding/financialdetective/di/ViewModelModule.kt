package com.coding.financialdetective.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coding.core.di.AppScope
import com.coding.core.di.ViewModelKey
import com.coding.financialdetective.app.MainViewModel
import com.coding.feature_categories.ui.CategoriesViewModel
import com.coding.feature_settings.ui.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @AppScope
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    abstract fun bindCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}