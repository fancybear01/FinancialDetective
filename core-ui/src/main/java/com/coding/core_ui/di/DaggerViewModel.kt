package com.coding.core_ui.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified T : ViewModel> daggerViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory
): T = viewModel(modelClass = T::class.java, key = key, factory = factory)