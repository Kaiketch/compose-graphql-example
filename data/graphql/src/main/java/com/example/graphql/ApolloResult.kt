package com.example.graphql

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

data class ApolloResult<T : Query.Data>(
    val data: T?,
    val isLoading: Boolean
) {
    companion object {
        fun <T : Query.Data> startLoading(): ApolloResult<T> {
            return ApolloResult(
                data = null,
                isLoading = true
            )
        }

        fun <T : Query.Data> success(
            response: ApolloResponse<T>
        ): ApolloResult<T> {
            return ApolloResult(
                data = response.data,
                isLoading = false
            )
        }

        fun <T : Query.Data> error(): ApolloResult<T> {
            return ApolloResult(
                data = null,
                isLoading = false
            )
        }
    }
}

data class ApolloMutationResult<T : Mutation.Data>(
    val data: T?,
    val isLoading: Boolean
) {
    companion object {
        fun <T : Mutation.Data> startLoading(): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = null,
                isLoading = true
            )
        }

        fun <T : Mutation.Data> success(
            response: ApolloResponse<T>
        ): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = response.data,
                isLoading = false
            )
        }

        fun <T : Mutation.Data> error(): ApolloMutationResult<T> {
            return ApolloMutationResult(
                data = null,
                isLoading = false
            )
        }
    }
}
