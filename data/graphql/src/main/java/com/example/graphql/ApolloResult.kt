package com.example.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import kotlinx.coroutines.flow.*

fun <T : Query.Data> ApolloClient.watchAsFlow(
    query: Query<T>,
    fetchPolicy: FetchPolicy? = null,
): Flow<ApolloResult<T>> {
    return flow {
        emit(ApolloResult.startLoading())
        emitAll(
            query(query).apply { fetchPolicy?.let { fetchPolicy(it) } }
                .watch().map {
                    ApolloResult.create(response = it)
                }.catch {
                    emit(ApolloResult.error(it))
                }
        )
    }
}

fun <T : Query.Data> ApolloClient.queryAsFlow(
    query: Query<T>,
    fetchPolicy: FetchPolicy? = null,
): Flow<ApolloResult<T>> {
    return flow {
        emit(ApolloResult.startLoading())
        emitAll(
            query(query).apply { fetchPolicy?.let { fetchPolicy(it) } }
                .toFlow().map {
                    ApolloResult.create(response = it)
                }.catch {
                    emit(ApolloResult.error(it))
                }
        )
    }
}

fun <T : Mutation.Data> ApolloClient.mutationAsFlow(
    mutation: Mutation<T>,
): Flow<ApolloMutationResult<T>> {
    return flow {
        emit(ApolloMutationResult.startLoading())
        emitAll(
            mutation(mutation)
                .toFlow().map {
                    ApolloMutationResult.create(response = it)
                }.catch {
                    emit(ApolloMutationResult.error(it))
                }
        )
    }
}

data class ApolloResult<T : Query.Data>(
    val data: T?,
    val isLoading: Boolean,
    val errors: List<Throwable>?
) {
    companion object {
        fun <T : Query.Data> startLoading(): ApolloResult<T> {
            return ApolloResult(
                data = null,
                isLoading = true,
                errors = null,
            )
        }

        fun <T : Query.Data> create(
            response: ApolloResponse<T>
        ): ApolloResult<T> {
            return ApolloResult(
                data = response.data,
                isLoading = false,
                errors = response.errors?.map { Exception(it.message) },
            )
        }

        fun <T : Query.Data> error(
            response: ApolloResponse<T>,
        ): ApolloResult<T> {
            return ApolloResult(
                data = null,
                isLoading = false,
                errors = response.errors?.map { Exception(it.message) },
            )
        }

        fun <T : Query.Data> error(
            cause: Throwable,
        ): ApolloResult<T> {
            return ApolloResult(
                data = null,
                isLoading = false,
                errors = listOf(cause),
            )
        }
    }
}

data class ApolloMutationResult<T : Mutation.Data>(
    val data: T?,
    val isLoading: Boolean,
    val errors: List<Throwable>?,
) {
    companion object {
        fun <T : Mutation.Data> startLoading(): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = null,
                isLoading = true,
                errors = null,
            )
        }

        fun <T : Mutation.Data> create(
            response: ApolloResponse<T>
        ): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = response.data,
                isLoading = false,
                errors = response.errors?.map { Exception(it.message) },
            )
        }

        fun <T : Mutation.Data> error(
            response: ApolloResponse<T>,
        ): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = null,
                isLoading = false,
                errors = response.errors?.map { Exception(it.message) },
            )
        }

        fun <T : Mutation.Data> error(
            cause: Throwable,
        ): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = null,
                isLoading = false,
                errors = listOf(cause),
            )
        }
    }
}
