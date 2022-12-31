package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.ViewerQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apolloClient: ApolloClient
) : ViewModel() {

    data class HomeUiState(
        val viewer: ViewerQuery.Viewer? = null,
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            runCatching {
                apolloClient.query(ViewerQuery()).execute().data?.viewer ?: throw IllegalStateException()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    viewer = it,
                )
            }.onFailure {

            }
        }
    }
}
