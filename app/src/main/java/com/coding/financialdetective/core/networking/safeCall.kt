package com.coding.financialdetective.core.networking

import com.coding.financialdetective.core.domain.util.Result
import com.coding.financialdetective.core.domain.util.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch(e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch(e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> safeCallWithRetry(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 2000,
    crossinline execute: suspend () -> HttpResponse
): Result<T, NetworkError> {

    for (attempt in 1..maxRetries) {
        val result = safeCall<T> { execute() }

        when (result) {
            is Result.Success -> {
                return result
            }
            is Result.Error -> {
                if (result.error != NetworkError.SERVER_ERROR || attempt == maxRetries) {
                    return result
                }
            }
        }

        delay(initialDelayMillis)
    }

    @Suppress("UNREACHABLE_CODE")
    return Result.Error(NetworkError.UNKNOWN)
}