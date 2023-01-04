package com.example.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.common.R

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navigateToDetail: (owner: String, name: String) -> Unit,
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    val user = searchUiState.result?.data?.user
    val isLoading = searchUiState.result?.isLoading ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.title_search,
                            user?.name.orEmpty()
                        ),
                    )
                }
            )
        }
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
