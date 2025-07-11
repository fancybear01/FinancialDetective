package com.coding.financialdetective.di

import com.coding.core.di.AppScope
import com.coding.core.data.remote.service.HttpClientFactory
import com.coding.core.data.remote.service.TokenProvider
import com.coding.financialdetective.util.AppTokenProvider
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

@Module
object NetworkModule {
    @Provides
    @AppScope
    fun provideTokenProvider(): TokenProvider = AppTokenProvider()

    @Provides
    @AppScope
    fun provideHttpClientEngine(): HttpClientEngine = CIO.create()

    @Provides
    @AppScope
    fun provideHttpClientFactory(tokenProvider: TokenProvider): HttpClientFactory {
        return HttpClientFactory(tokenProvider)
    }

    @Provides
    @AppScope
    fun provideHttpClient(
        factory: HttpClientFactory,
        engine: HttpClientEngine
    ): HttpClient {
        return factory.create(engine)
    }
}