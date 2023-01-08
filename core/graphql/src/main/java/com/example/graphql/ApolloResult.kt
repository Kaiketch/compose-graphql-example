package com.example.graphql

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

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
