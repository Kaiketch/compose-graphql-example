package com.example.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.RepositoryQuery
import com.example.common.Param
import com.example.fragment.Repository
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
        Log.d("detail viewmodel", "param $owner $name")
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
}
