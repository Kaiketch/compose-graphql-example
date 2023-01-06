package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.example.graphql.ApolloResult
import com.example.graphql.RepositoriesQuery
import com.example.graphql.ViewerQuery
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ViewerRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun fetchViewer(
        fetchPolicy: FetchPolicy = FetchPolicy.CacheFirst
    ): Flow<ApolloResult<ViewerQuery.Data>> {
        return flow {
            emit(ApolloResult.startLoading())
            emitAll(
                apolloClient.query(ViewerQuery())
                    .apply { fetchPolicy(fetchPolicy) }.watch().map {
                    ApolloResult.success(response = it)
                }.catch {
                    emit(ApolloResult.error())
                }
            )
        }
    }
}
