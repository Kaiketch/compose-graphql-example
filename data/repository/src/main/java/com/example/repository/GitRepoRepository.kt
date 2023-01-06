package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GitRepoRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun fetchRepositories(
        keyword: String,
        limit: Int,
        fetchPolicy: FetchPolicy = FetchPolicy.NetworkOnly
    ): Flow<ApolloResult<RepositoriesQuery.Data>> {
        return flow {
            emit(ApolloResult.startLoading())
            emitAll(
                apolloClient.query(RepositoriesQuery(keyword, limit))
                    .apply { fetchPolicy(fetchPolicy) }.watch().map {
                    ApolloResult.success(response = it)
                }.catch {
                    emit(ApolloResult.error())
                }
            )
        }
    }
}
