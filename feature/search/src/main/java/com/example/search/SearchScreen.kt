package com.example.search

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val uiState = searchViewModel.uiState.collectAsState()

    Text(text = uiState.value.viewer?.login ?: "empty")
}
