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
        val keyword: String = "",
        val result: ApolloResult<RepositoriesQuery.Data>? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        viewModelScope.launch {
            val login = apolloClient.query(ViewerQuery()).toFlow().first().data?.viewer?.login
                ?: return@launch
            _uiState.value = _uiState.value.copy(keyword = login)

            val limit = settingDataStore.getRequestLimit().first()
            fetchRepositories(login, limit).collect { result ->
                _uiState.value = _uiState.value.copy(result = result)
            }
        }
    }

    fun onKeywordChanged(keyword: String) {
        _uiState.value = _uiState.value.copy(keyword = keyword)
    }

    fun onSearchClicked() {
        viewModelScope.launch {
            val login = _uiState.value.keyword
            val limit = settingDataStore.getRequestLimit().first()
            fetchRepositories(login, limit).collect { result ->
                _uiState.value = _uiState.value.copy(result = result)
            }
        }
    }

    private fun fetchRepositories(
        keyword: String,
        limit: Int
    ): Flow<ApolloResult<RepositoriesQuery.Data>> {
        return flow {
            emit(ApolloResult.startLoading())
            emitAll(
                apolloClient.query(RepositoriesQuery(keyword, limit)).watch().map {
                    ApolloResult.success(response = it)
                }.catch {
                    emit(ApolloResult.error())
                }
            )
        }
    }
}
