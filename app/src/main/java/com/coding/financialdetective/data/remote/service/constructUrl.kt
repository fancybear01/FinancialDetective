package com.coding.financialdetective.data.remote.service

val BASE_URL = "https://shmr-finance.ru/api/v1/"

fun constructUrl(url: String): String {
    return when {
        url.contains(BASE_URL) -> url
        url.startsWith("/") -> BASE_URL + url.drop(1)
        else -> BASE_URL + url
    }
}