package com.example.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.R

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchUiState by searchViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.title_search,
                            searchUiState.user?.name.orEmpty()
                        ),
                    )
                }
            )
        }
    ) { paddingValues ->
        val repositories =
            searchUiState.user?.repositories?.nodes?.map { node -> node?.repository!! }
        repositories?.let {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(it) { item ->
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .clickable {
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                    Divider()
                }
            }
        }
    }
}
