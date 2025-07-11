package com.coding.core.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(private val tokenProvider: TokenProvider) {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(Auth) {
                bearer {
                    loadTokens {
                        tokenProvider.getToken()?.let { token ->
                            BearerTokens(accessToken = token, refreshToken = "")
                        }
                    }
                    refreshTokens {
                        tokenProvider.refreshToken()
                    }
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}

interface TokenProvider {
    fun getToken(): String?
    fun refreshToken(): BearerTokens?
}