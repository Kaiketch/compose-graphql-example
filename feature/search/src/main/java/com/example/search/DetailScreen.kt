package com.example.search

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.common.R
import com.example.common.component.ErrorDialog
import com.example.common.component.ProgressView
import com.example.common.theme.ComposeGraphQLExampleTheme
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoryQuery
import com.example.graphql.fragment.Repository

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
) {
    val detailUiState by detailViewModel.uiState.collectAsState()

    DetailContent(
        detailUiState = detailUiState,
        onStarIconTapped = { detailViewModel.onStarIconTapped() },
    )
}

@Composable
fun DetailContent(
    detailUiState: DetailViewModel.DetailUiState,
    onStarIconTapped: () -> Unit,
) {
    val repository = detailUiState.result?.data?.repository?.repository
    val isLoading = detailUiState.result?.isLoading ?: false
            || detailUiState.addStarResult?.isLoading ?: false
            || detailUiState.removeStarResult?.isLoading ?: false
    val errors = detailUiState.result?.errors ?: detailUiState.addStarResult?.errors
    ?: detailUiState.removeStarResult?.errors

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
                    .fillMaxWidth()
                    .padding(16.dp)
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
                            onStarIconTapped()
                        }
                    )
                } else {
                    Icon(
                        Icons.Default.StarOutline, null,
                        modifier = Modifier.clickable {
                            onStarIconTapped()
                        }
                    )
                }
            }
            Text(
                text = repository?.url.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        if (errors != null) {
            ErrorDialog(errors = errors)
        }

        if (isLoading) {
            ProgressView()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.FROYO)
@Preview(
    name = "light mode",
    group = "ui modes",
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "night mode",
    group = "ui modes",
    uiMode = UI_MODE_NIGHT_YES
)
annotation class UiModePreviews

@UiModePreviews
@Composable
fun DetailContentPreview(
    @PreviewParameter(DetailUiStateProvider::class) detailUiState: DetailViewModel.DetailUiState,
) {
    ComposeGraphQLExampleTheme {
        DetailContent(
            detailUiState = detailUiState,
            onStarIconTapped = {}
        )
    }
}

class DetailUiStateProvider : PreviewParameterProvider<DetailViewModel.DetailUiState> {
    override val values: Sequence<DetailViewModel.DetailUiState> = sequenceOf(
        DetailViewModel.DetailUiState(
            result = ApolloResult(
                data = RepositoryQuery.Data(
                    RepositoryQuery.Repository(
                        "repository",
                        Repository(
                            id = "1",
                            name = "repo name",
                            owner = Repository.Owner(
                                id = "1",
                                login = "me",
                            ),
                            description = null,
                            createdAt = "",
                            updatedAt = "",
                            url = "https://xxx.com",
                            stargazerCount = 1,
                            viewerHasStarred = true,
                        )
                    )
                ),
                isLoading = false,
                errors = null,
            )
        ),
        DetailViewModel.DetailUiState(
            result = ApolloResult(
                data = null,
                isLoading = true,
                errors = null,
            )
        )
    )
}
