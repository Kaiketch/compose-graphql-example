package com.example.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.example.common.Param
import com.example.graphql.*
import com.example.repository.GitRepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gitRepoRepository: GitRepoRepository,
) : ViewModel() {

    data class DetailUiState(
        val result: ApolloResult<RepositoryQuery.Data>? = null,
        val addStarResult: ApolloMutationResult<AddStarMutation.Data>? = null,
        val removeStarResult: ApolloMutationResult<RemoveStarMutation.Data>? = null,
    )

    private val owner = savedStateHandle.get<String>(Param.OWNER)
    private val name = savedStateHandle.get<String>(Param.NAME)

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        if (owner != null && name != null) {
            gitRepoRepository.watchRepository(owner, name)
                .onEach { result ->
                    _uiState.value = _uiState.value.copy(result = result)
                }.catch {
                }.launchIn(viewModelScope)
        }
    }

    fun onStarIconTapped() {
        val id =
            _uiState.value.result?.data?.repository?.repository?.id ?: throw IllegalStateException()
        if (_uiState.value.result?.data?.repository?.repository?.viewerHasStarred == true) {
            removeStar(id)
        } else {
            addStar(id)
        }
    }

    private fun addStar(id: String) {
        viewModelScope.launch {
            gitRepoRepository.addStar(id).collect {
                _uiState.value = _uiState.value.copy(addStarResult = it)
                if (owner != null && name != null) {
                    gitRepoRepository.fetchRepository(owner, name, FetchPolicy.NetworkOnly)
                        .collect()
                }
            }
        }
    }

    private fun removeStar(id: String) {
        viewModelScope.launch {
            gitRepoRepository.removeStar(id).collect {
                _uiState.value = _uiState.value.copy(removeStarResult = it)
                if (owner != null && name != null) {
                    gitRepoRepository.fetchRepository(owner, name, FetchPolicy.NetworkOnly)
                        .collect()
                }
            }
        }
    }
}
