package com.coding.financialdetective.data.remote.service

import com.coding.core.data.remote.service.TokenProvider
import com.coding.financialdetective.BuildConfig
import io.ktor.client.plugins.auth.providers.BearerTokens

class AppTokenProvider : TokenProvider {
    override fun getToken(): String? = BuildConfig.API_KEY

    override fun refreshToken(): BearerTokens? {
        return null
    }
}