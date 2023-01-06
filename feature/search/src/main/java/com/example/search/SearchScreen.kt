package com.example.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navigateToDetail: (owner: String, name: String) -> Unit,
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    val user = searchUiState.result?.data?.user
    val isLoading = searchUiState.result?.isLoading ?: false

    LaunchedEffect(searchViewModel) {
        searchViewModel.onResume()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                title = {
                    OutlinedTextField(
                        value = searchUiState.keyword,
                        onValueChange = { searchViewModel.onKeywordChanged(it) },
                        modifier = Modifier.padding(8.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { searchViewModel.onSearchClicked() }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        val repositories = user?.repositories?.nodes?.map { node -> node?.repository!! }
        repositories?.let {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(it) { item ->
                    Row(
                        modifier = Modifier
                            .clickable {
                                navigateToDetail(
                                    item.owner.login,
                                    item.name
                                )
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = item.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = item.stargazerCount.toString()
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        if (item.viewerHasStarred) {
                            Icon(Icons.Default.Star, null)
                        } else {
                            Icon(Icons.Default.StarOutline, null)
                        }
                    }
                    Divider()
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
