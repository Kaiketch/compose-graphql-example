package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.watch
import com.example.RepositoriesQuery
import com.example.ViewerQuery
import com.example.datastore.SettingDataStore
import com.example.graphql.ApolloResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val settingDataStore: SettingDataStore
) : ViewModel() {

    data class SearchUiState(
        val result: ApolloResult<RepositoriesQuery.Data>? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        viewModelScope.launch {
            runCatching {
                apolloClient.query(ViewerQuery()).execute().data?.viewer
                    ?: throw IllegalStateException()
            }.onSuccess {
                val limit = settingDataStore.getRequestLimit().first()
                flow {
                    emit(ApolloResult.startLoading())
                    emitAll(
                        apolloClient.query(RepositoriesQuery(it.login, limit)).watch().map {
                            ApolloResult.success(response = it)
                        }.catch {
                            emit(ApolloResult.error())
                        }
                    )
                }.collect { result ->
                    _uiState.value = _uiState.value.copy(result = result)
                }
            }.onFailure {

            }
        }
    }
}
