package com.coding.financialdetective.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.coding.core.di.AppDependencies
import com.coding.core.di.AppScope
import com.coding.financialdetective.app.MainActivity
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AppDependencies {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    override fun viewModelFactory(): ViewModelProvider.Factory

    fun inject(activity: MainActivity)
}