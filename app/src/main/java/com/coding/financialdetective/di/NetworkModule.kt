package com.coding.financialdetective.di

import com.coding.financialdetective.data.remote.service.AppTokenProvider
import com.coding.financialdetective.data.remote.service.HttpClientFactory
import com.coding.financialdetective.data.remote.service.TokenProvider
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module
import io.ktor.client.engine.cio.CIO

val networkModule = module {
    single<TokenProvider> { AppTokenProvider() }

    single<HttpClientEngine> { CIO.create() }

    single { HttpClientFactory(get()) }

    single {
        get<HttpClientFactory>().create(get())
    }
}