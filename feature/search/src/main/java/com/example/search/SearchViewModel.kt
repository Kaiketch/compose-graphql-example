package com.example.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.example.RepositoriesQuery
import com.example.ViewerQuery
import com.example.datastore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val settingDataStore: SettingDataStore
) : ViewModel() {

    data class SearchUiState(
        val user: RepositoriesQuery.User? = null,
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        viewModelScope.launch {
            runCatching {
                apolloClient.query(ViewerQuery()).execute().data?.viewer
                    ?: throw IllegalStateException()
            }.mapCatching {
                val limit = settingDataStore.getRequestLimit().first()
                apolloClient.query(RepositoriesQuery(it.login, limit)).execute().data?.user
                    ?: throw IllegalStateException()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    user = it,
                )
            }.onFailure {

            }
        }
    }
}
