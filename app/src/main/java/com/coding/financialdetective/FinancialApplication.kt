package com.coding.financialdetective

import android.app.Application
import android.content.Context
import com.coding.financialdetective.di.AppComponent
import com.coding.financialdetective.di.DaggerAppComponent

/**
 * Реализация инъекии зависимостей приложения
 */
val Context.appComponent: AppComponent
    get() = (this.applicationContext as FinancialApplication).appComponent

class FinancialApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}