package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastore.SettingDataStore
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import com.example.graphql.ViewerQuery
import com.example.repository.GitRepoRepository
import com.example.repository.ViewerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val viewerRepository: ViewerRepository,
    private val gitRepoRepository: GitRepoRepository,
    private val settingDataStore: SettingDataStore,
) : ViewModel() {

    data class SearchUiState(
        val keyword: String = "",
        val limit: Int = 0,
        val viewerResult: ApolloResult<ViewerQuery.Data>? = null,
        val result: ApolloResult<RepositoriesQuery.Data>? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onResume() {
        viewModelScope.launch {
            val limit = settingDataStore.getRequestLimit().first()
            if (_uiState.value.limit != limit) {
                if (_uiState.value.keyword.isEmpty()) {
                    viewerRepository.fetchViewer().collect { viewerResult ->
                        val login = viewerResult.data?.viewer?.login ?: return@collect
                        _uiState.value =
                            _uiState.value.copy(viewerResult = viewerResult, keyword = login)
                        gitRepoRepository.fetchRepositories(login, limit)
                            .collect { result ->
                                _uiState.value = _uiState.value.copy(result = result, limit = limit)
                            }
                    }
                } else {
                    gitRepoRepository.fetchRepositories(_uiState.value.keyword, limit)
                        .collect { result ->
                            _uiState.value = _uiState.value.copy(result = result, limit = limit)
                        }
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
