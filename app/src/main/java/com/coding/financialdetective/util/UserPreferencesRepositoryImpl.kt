package com.coding.financialdetective.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coding.core.domain.repository.ColorSchemeSetting
import com.coding.core.domain.repository.HapticFeedbackEffect
import com.coding.core.domain.repository.LanguageSetting
import com.coding.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl @Inject constructor(
    private val context: Context
) : UserPreferencesRepository {

    object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val COLOR_SCHEME = stringPreferencesKey("color_scheme")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val HAPTIC_EFFECT = stringPreferencesKey("haptic_effect")
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    override val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: false
        }

    override suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }

    override val colorSchemeSetting: Flow<ColorSchemeSetting> = context.dataStore.data
        .map { preferences ->
            val schemeName =
                preferences[PreferencesKeys.COLOR_SCHEME] ?: ColorSchemeSetting.GREEN.name
            try {
                ColorSchemeSetting.valueOf(schemeName)
            } catch (e: IllegalArgumentException) {
                ColorSchemeSetting.GREEN
            }
        }

    override suspend fun setColorSchemeSetting(scheme: ColorSchemeSetting) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COLOR_SCHEME] = scheme.name
        }
    }

    override val hapticsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.HAPTICS_ENABLED] ?: true }

    override val hapticEffect: Flow<HapticFeedbackEffect> = context.dataStore.data
        .map { preferences ->
            val effectName =
                preferences[PreferencesKeys.HAPTIC_EFFECT] ?: HapticFeedbackEffect.LIGHT.name
            try {
                HapticFeedbackEffect.valueOf(effectName)
            } catch (e: IllegalArgumentException) {
                HapticFeedbackEffect.LIGHT
            }
        }

    override suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.HAPTICS_ENABLED] = enabled }
    }

    override suspend fun setHapticEffect(effect: HapticFeedbackEffect) {
        context.dataStore.edit { it[PreferencesKeys.HAPTIC_EFFECT] = effect.name }
    }

    override val languageSetting: Flow<LanguageSetting> = context.dataStore.data
        .map { preferences ->
            val code = preferences[PreferencesKeys.LANGUAGE_CODE] ?: LanguageSetting.RUSSIAN.code
            LanguageSetting.values().firstOrNull { it.code == code } ?: LanguageSetting.RUSSIAN
        }

    override suspend fun setLanguageSetting(language: LanguageSetting) {
        context.dataStore.edit { it[PreferencesKeys.LANGUAGE_CODE] = language.code }
    }
}