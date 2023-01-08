package com.example.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.common.R
import com.example.common.component.ProgressView

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
) {
    val detailUiState by detailViewModel.uiState.collectAsState()
    val repository = detailUiState.result?.data?.repository?.repository
    val isLoading = detailUiState.result?.isLoading ?: false
            || detailUiState.addStarResult?.isLoading ?: false
            || detailUiState.removeStarResult?.isLoading ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.title_detail,
                            repository?.name.orEmpty()
                        ),
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = repository?.name.orEmpty())
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                )
                Text(
                    text = repository?.stargazerCount.toString()
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (repository?.viewerHasStarred == true) {
                    Icon(
                        Icons.Default.Star, null,
                        modifier = Modifier.clickable {
                            detailViewModel.onStarIconTapped()
                        }
                    )
                } else {
                    Icon(
                        Icons.Default.StarOutline, null,
                        modifier = Modifier.clickable {
                            detailViewModel.onStarIconTapped()
                        }
                    )
                }
            }
            Text(
                text = repository?.url.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isLoading) {
            ProgressView()
        }
    }
}
