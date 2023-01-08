package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.RequestLimit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SETTING
    )

    suspend fun saveRequestLimit(limit: Int): Int {
        context.dataStore.edit { it[REQUEST_LIMIT] = limit }
        return limit
    }

    fun getRequestLimit(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[REQUEST_LIMIT] ?: RequestLimit.TWENTY.limit
        }
    }

    companion object {
        const val SETTING = "setting"
        val REQUEST_LIMIT = intPreferencesKey("request_limit")
    }
}
