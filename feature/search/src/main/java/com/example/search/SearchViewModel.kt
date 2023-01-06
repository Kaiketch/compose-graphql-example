package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.datastore.SettingDataStore
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import com.example.graphql.ViewerQuery
import com.example.repository.GitRepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val gitRepoRepository: GitRepoRepository,
    private val settingDataStore: SettingDataStore,
) : ViewModel() {

    data class SearchUiState(
        val keyword: String = "",
        val limit: Int = 0,
        val result: ApolloResult<RepositoriesQuery.Data>? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onResume() {
        viewModelScope.launch {
            val limit = settingDataStore.getRequestLimit().first()
            if (_uiState.value.limit != limit) {
                val login = if (_uiState.value.keyword.isNullOrEmpty()) {
                    apolloClient.query(ViewerQuery()).toFlow().first().data?.viewer?.login
                } else {
                    _uiState.value.keyword
                } ?: return@launch

                gitRepoRepository.fetchRepositories(login, limit).collect { result ->
                    _uiState.value =
                        _uiState.value.copy(result = result, keyword = login, limit = limit)
                }
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
            gitRepoRepository.fetchRepositories(login, limit).collect { result ->
                _uiState.value = _uiState.value.copy(result = result)
            }
        }
    }
}
