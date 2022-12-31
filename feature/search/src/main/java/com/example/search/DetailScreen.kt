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

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
) {
    val uiState by detailViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.title_detail,
                            uiState.repository?.name.orEmpty()
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
                Text(text = uiState.repository?.name.orEmpty())
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                )
                Text(
                    text = uiState.repository?.stargazerCount.toString()
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (uiState.repository?.viewerHasStarred == true) {
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
                text = uiState.repository?.url.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
