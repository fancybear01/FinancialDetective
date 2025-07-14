package com.coding.financialdetective.di

import android.content.Context
import androidx.room.Room
import com.coding.core.di.AppScope
import com.coding.financialdetective.util.AndroidConnectivityObserver
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.util.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @Provides
    @AppScope
    fun provideConnectivityObserver(context: Context): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }

    @Provides
    @AppScope
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "financial_detective_db"
        ).fallbackToDestructiveMigration() .build()
    }

    @Provides
    @AppScope
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()

    @Provides
    @AppScope
    fun provideAccountDao(db: AppDatabase) = db.accountDao()

    @Provides
    @AppScope
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()
}