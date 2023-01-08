package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import com.example.graphql.ViewerQuery
import com.example.model.Result
import com.example.repository.GitRepoRepository
import com.example.repository.SettingRepository
import com.example.repository.ViewerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val viewerRepository: ViewerRepository,
    private val gitRepoRepository: GitRepoRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    data class SearchUiState(
        val keyword: String = "",
        val viewerResult: ApolloResult<ViewerQuery.Data>? = null,
        val result: ApolloResult<RepositoriesQuery.Data>? = null,
        val limitResult: Result<Int>? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onResume() {
        viewModelScope.launch {
            settingRepository.fetchRequestLimit().collect { limitResult ->
                val limit = limitResult.data ?: return@collect
                if (_uiState.value.limitResult?.data != limit) {
                    if (_uiState.value.keyword.isEmpty()) {
                        viewerRepository.fetchViewer().collect { viewerResult ->
                            val login = viewerResult.data?.viewer?.login ?: return@collect
                            _uiState.value =
                                _uiState.value.copy(viewerResult = viewerResult, keyword = login)
                            gitRepoRepository.watchRepositories(login, limit)
                                .collect { result ->
                                    _uiState.value =
                                        _uiState.value.copy(
                                            result = result,
                                            limitResult = limitResult
                                        )
                                }
                        }
                    } else {
                        gitRepoRepository.watchRepositories(_uiState.value.keyword, limit)
                            .collect { result ->
                                _uiState.value =
                                    _uiState.value.copy(result = result, limitResult = limitResult)
                            }
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
            settingRepository.fetchRequestLimit().collect { limitResult ->
                val limit = limitResult.data ?: return@collect
                gitRepoRepository.watchRepositories(login, limit).collect { result ->
                    _uiState.value = _uiState.value.copy(result = result)
                }
            }
        }
    }
}
