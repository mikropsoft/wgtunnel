package com.zaneschepke.wireguardautotunnel.data.repository

import com.zaneschepke.wireguardautotunnel.data.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun save(settings : Settings)
    fun getSettings() : Flow<Settings>

    suspend fun getAll() : List<Settings>
}