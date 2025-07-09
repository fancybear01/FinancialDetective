package com.coding.financialdetective

import android.app.Application
import com.coding.core_ui.di.AppDependenciesProvider
import com.coding.financialdetective.di.AppComponent
import com.coding.financialdetective.di.DaggerAppComponent

class FinancialApplication : Application(), AppDependenciesProvider {
    override val dependencies: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}