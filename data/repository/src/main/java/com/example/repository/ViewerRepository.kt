package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.example.graphql.ApolloResult
import com.example.graphql.ViewerQuery
import com.example.graphql.watchAsFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewerRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun fetchViewer(
        fetchPolicy: FetchPolicy = FetchPolicy.CacheFirst
    ): Flow<ApolloResult<ViewerQuery.Data>> {
        return apolloClient.watchAsFlow(ViewerQuery(), fetchPolicy)
    }
}
