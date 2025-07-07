package com.coding.financialdetective.di

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsIncome

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionTypeQualifier