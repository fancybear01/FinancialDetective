package com.coding.core.data.remote.service

import android.util.Log
import com.coding.core.data.util.NetworkError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.coding.core.data.util.Result

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> {
            if (T::class == Unit::class) {
                @Suppress("UNCHECKED_CAST")
                Result.Success(Unit as T)
            } else {
                try {
                    Result.Success(response.body<T>())
                } catch (e: Exception) {
                    Log.e("RESPONSE_PARSING_ERROR", "Failed to parse response body", e)
                    Result.Error(NetworkError.SERIALIZATION)
                }
            }
        }
        400 -> Result.Error(NetworkError.BAD_REQUEST)
        401 -> Result.Error(NetworkError.UNAUTHORIZED)
        403 -> Result.Error(NetworkError.FORBIDDEN)
        404 -> Result.Error(NetworkError.NOT_FOUND)
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}