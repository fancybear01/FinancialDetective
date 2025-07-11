package com.coding.core.data.remote.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Наблюдатель за состоянием сетевого подключения.
 * Предоставляет актуальную информацию о наличии соединения с интернетом.
 */
interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}