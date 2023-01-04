package com.example.graphql

import com.apollographql.apollo3.api.ApolloResponse
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
