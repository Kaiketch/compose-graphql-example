package com.example.repository

import com.example.datastore.SettingDataStore
import com.example.model.Result
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    private val settingDataStore: SettingDataStore,
) {
    fun fetchRequestLimit(): Flow<Result<Int>> {
        return flow {
            emit(Result.startLoading())
            emitAll(
                settingDataStore.getRequestLimit().map {
                    Result.success(it)
                }.catch {
                    Result.error<Any>(it)
                }
            )
        }
    }

    fun editRequestLimit(limit: Int): Flow<Result<Int>> {
        return flow {
            emit(Result.startLoading())
            runCatching {
                settingDataStore.saveRequestLimit(limit)
            }.onSuccess {
                emit(Result.success(it))
            }.onFailure {
                emit(Result.error(it))
            }
        }
    }
}
