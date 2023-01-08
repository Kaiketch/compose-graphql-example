package com.example.repository

import com.example.datastore.SettingDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    private val settingDataStore: SettingDataStore,
) {
    fun fetchRequestLimit(): Flow<Int> {
        return settingDataStore.getRequestLimit()
    }

    suspend fun editRequestLimit(limit: Int) {
        settingDataStore.saveRequestLimit(limit)
    }
}
