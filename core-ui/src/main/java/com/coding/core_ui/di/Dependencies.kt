package com.coding.core_ui.di

import android.content.Context
import com.coding.core.di.AppDependencies

interface AppDependenciesProvider {
    val dependencies: AppDependencies
}

val Context.appDependencies: AppDependencies
    get() = (this.applicationContext as AppDependenciesProvider).dependencies