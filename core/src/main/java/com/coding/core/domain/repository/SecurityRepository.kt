package com.coding.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SecurityRepository {
    val hasPinCode: Flow<Boolean>

    suspend fun setPinCode(pin: String)

    suspend fun checkPinCode(pin: String): Boolean

    suspend fun clearPinCode()
}