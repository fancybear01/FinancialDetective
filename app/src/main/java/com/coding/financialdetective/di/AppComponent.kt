package com.coding.financialdetective.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.coding.financialdetective.MainActivity
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
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelFactory(): ViewModelProvider.Factory

    fun accountFeatureComponent(): AccountFeatureComponent.Factory

    fun transactionFeatureComponent(): TransactionFeatureComponent.Factory

    fun inject(activity: MainActivity)
}