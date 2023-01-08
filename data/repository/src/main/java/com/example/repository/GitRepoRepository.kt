package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.example.graphql.*
import com.example.graphql.type.AddStarInput
import com.example.graphql.type.RemoveStarInput
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GitRepoRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun watchRepositories(
        keyword: String,
        limit: Int,
        fetchPolicy: FetchPolicy? = null
    ): Flow<ApolloResult<RepositoriesQuery.Data>> {
        return apolloClient.watchAsFlow(RepositoriesQuery(keyword, limit), fetchPolicy)
    }

    fun watchRepository(
        owner: String,
        name: String,
        fetchPolicy: FetchPolicy? = null
    ): Flow<ApolloResult<RepositoryQuery.Data>> {
        return apolloClient.watchAsFlow(RepositoryQuery(owner, name), fetchPolicy)
    }

    // mutation後のキャッシュ更新用。本来であればmutationの返り値で。
    fun fetchRepository(
        owner: String,
        name: String,
        fetchPolicy: FetchPolicy? = null
    ): Flow<ApolloResult<RepositoryQuery.Data>> {
        return apolloClient.queryAsFlow(RepositoryQuery(owner, name), fetchPolicy)
    }

    fun addStar(id: String): Flow<ApolloMutationResult<AddStarMutation.Data>> {
        val input = AddStarInput(starrableId = id)
        return apolloClient.mutationAsFlow(AddStarMutation(input))
    }

    fun removeStar(id: String): Flow<ApolloMutationResult<RemoveStarMutation.Data>> {
        val input = RemoveStarInput(starrableId = id)
        return apolloClient.mutationAsFlow(RemoveStarMutation(input))
    }
}
