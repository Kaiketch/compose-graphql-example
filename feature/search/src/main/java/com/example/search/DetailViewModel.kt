package com.example.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.AddStarMutation
import com.example.RemoveStarMutation
import com.example.RepositoryQuery
import com.example.common.Param
import com.example.fragment.Repository
import com.example.type.AddStarInput
import com.example.type.RemoveStarInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        if (owner != null && name != null) {
            viewModelScope.launch {
                runCatching {
                    apolloClient.query(RepositoryQuery(owner, name))
                        .execute().data?.repository?.repository
                        ?: throw IllegalStateException()
                }.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        repository = it,
                    )
                }.onFailure {

                }
            }
        }
    }

    fun onStarIconTapped() {
        val id = _uiState.value.repository?.id ?: throw IllegalStateException()
        if (_uiState.value.repository?.viewerHasStarred == true) {
            removeStar(id)
        } else {
            addStar(id)
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
            }.onFailure {

            }
        }
    }
}
