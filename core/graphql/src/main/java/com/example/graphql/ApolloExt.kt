package com.example.graphql

import com.apollographql.apollo3.ApolloClient
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
