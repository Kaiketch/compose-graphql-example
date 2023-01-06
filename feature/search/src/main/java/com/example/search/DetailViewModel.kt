package com.example.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.example.graphql.AddStarMutation
import com.example.graphql.RemoveStarMutation
import com.example.graphql.RepositoryQuery
import com.example.common.Param
import com.example.graphql.fragment.Repository
import com.example.graphql.type.AddStarInput
import com.example.graphql.type.RemoveStarInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloClient: ApolloClient
) : ViewModel() {

    data class DetailUiState(
        val repository: Repository? = null,
    )

    private val owner = savedStateHandle.get<String>(Param.OWNER)
    private val name = savedStateHandle.get<String>(Param.NAME)

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        fetchRepository()
    }

    fun onStarIconTapped() {
        val id = _uiState.value.repository?.id ?: throw IllegalStateException()
        if (_uiState.value.repository?.viewerHasStarred == true) {
            removeStar(id)
        } else {
            addStar(id)
        }
    }

    private fun fetchRepository(
        fetchPolicy: FetchPolicy? = null
    ) {
        if (owner != null && name != null) {
            apolloClient.query(RepositoryQuery(owner, name))
                .apply {
                    fetchPolicy?.let { fetchPolicy(it) }
                }.toFlow()
                .onEach { response ->
                    val repo =
                        response.data?.repository?.repository ?: throw IllegalStateException()
                    _uiState.value = _uiState.value.copy(repository = repo)
                }.catch {
                }.launchIn(viewModelScope)
        }
    }

    private fun removeStar(id: String) {
        viewModelScope.launch {
            runCatching {
                val input = RemoveStarInput(starrableId = id)
                apolloClient.mutation(RemoveStarMutation(input))
                    .execute().data?.removeStar?.starrable
                    ?: throw IllegalStateException()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    repository = _uiState.value.repository?.copy(
                        stargazerCount = it.stargazerCount,
                        viewerHasStarred = it.viewerHasStarred,
                    ),
                )
                fetchRepository(FetchPolicy.NetworkOnly)
            }.onFailure {

            }
        }
    }

    private fun addStar(id: String) {
        viewModelScope.launch {
            runCatching {
                val input = AddStarInput(starrableId = id)
                apolloClient.mutation(AddStarMutation(input))
                    .execute().data?.addStar?.starrable
                    ?: throw IllegalStateException()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    repository = _uiState.value.repository?.copy(
                        stargazerCount = it.stargazerCount,
                        viewerHasStarred = it.viewerHasStarred,
                    ),
                )
                fetchRepository(FetchPolicy.NetworkOnly)
            }.onFailure {

            }
        }
    }
}
