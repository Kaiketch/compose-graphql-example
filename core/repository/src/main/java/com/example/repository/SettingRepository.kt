package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.example.datastore.SettingDataStore
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import com.example.graphql.ViewerQuery
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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
