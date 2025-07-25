package com.coding.financialdetective.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.coding.core.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    context: Context
) : SecurityRepository {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _hasPinCode = MutableStateFlow(sharedPreferences.contains(PIN_KEY))
    override val hasPinCode: Flow<Boolean> = _hasPinCode

    override suspend fun setPinCode(pin: String) {
        sharedPreferences.edit().putString(PIN_KEY, pin).apply()
        _hasPinCode.value = true
    }

    override suspend fun checkPinCode(pin: String): Boolean {
        val storedPin = sharedPreferences.getString(PIN_KEY, null)
        return storedPin == pin
    }

    override suspend fun clearPinCode() {
        sharedPreferences.edit().remove(PIN_KEY).apply()
        _hasPinCode.value = false
    }

    companion object {
        private const val PIN_KEY = "user_pin_code"
    }
}