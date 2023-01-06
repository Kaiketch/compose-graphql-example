package com.example.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.example.graphql.*
import com.example.graphql.type.AddStarInput
import com.example.graphql.type.RemoveStarInput
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GitRepoRepository @Inject constructor(
    private val apolloClient: ApolloClient,
) {
    fun fetchRepositories(
        keyword: String,
        limit: Int,
        fetchPolicy: FetchPolicy = FetchPolicy.CacheFirst
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

    fun fetchRepository(
        owner: String,
        name: String,
        fetchPolicy: FetchPolicy = FetchPolicy.CacheFirst
    ): Flow<ApolloResult<RepositoryQuery.Data>> {
        return flow {
            emit(ApolloResult.startLoading())
            emitAll(
                apolloClient.query(RepositoryQuery(owner, name))
                    .apply { fetchPolicy(fetchPolicy) }.watch().map {
                        ApolloResult.success(response = it)
                    }.catch {
                        emit(ApolloResult.error())
                    }
            )
        }
    }

    // mutation後のキャッシュ更新用。本来であればmutationの返り値で。
    fun reFetchRepository(
        owner: String,
        name: String,
    ): Flow<ApolloResult<RepositoryQuery.Data>> {
        return flow {
            emit(ApolloResult.startLoading())
            emitAll(
                apolloClient.query(RepositoryQuery(owner, name))
                    .fetchPolicy(FetchPolicy.NetworkOnly).toFlow().map {
                        ApolloResult.success(response = it)
                    }.catch {
                        emit(ApolloResult.error())
                    }
            )
        }
    }

    fun addStar(id: String): Flow<ApolloMutationResult<AddStarMutation.Data>> {
        val input = AddStarInput(starrableId = id)
        return flow {
            emit(ApolloMutationResult.startLoading())
            emitAll(
                apolloClient.mutation(AddStarMutation(input))
                    .toFlow().map {
                        ApolloMutationResult.success(response = it)
                    }.catch {
                        emit(ApolloMutationResult.error())
                    }
            )
        }
    }

    fun removeStar(id: String): Flow<ApolloMutationResult<RemoveStarMutation.Data>> {
        val input = RemoveStarInput(starrableId = id)
        return flow {
            emit(ApolloMutationResult.startLoading())
            emitAll(
                apolloClient.mutation(RemoveStarMutation(input))
                    .toFlow().map {
                        ApolloMutationResult.success(response = it)
                    }.catch {
                        emit(ApolloMutationResult.error())
                    }
            )
        }
    }
}
