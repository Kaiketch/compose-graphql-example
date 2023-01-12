package com.example.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.Executable
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.api.*
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApolloModule {

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .serverUrl("https://api.github.com/graphql")
            .fetchPolicy(FetchPolicy.CacheFirst)
            .normalizedCache(
                MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024),
                cacheKeyGenerator = object : CacheKeyGenerator {
                    override fun cacheKeyForObject(
                        obj: Map<String, Any?>,
                        context: CacheKeyGeneratorContext
                    ): CacheKey? {
                        val typename = context.field.type.leafType().name

                        if(typename == "Repository") {
                            val owner = (obj["owner"] as Map<*, *>)["login"]
                            val name = obj["name"]
                            if (owner is String && name is String) {
                                return CacheKey(typename, owner, name)
                            }
                        }

                        val id = obj["id"]
                        if (id is String) {
                            return CacheKey(typename, id)
                        }

                        return null
                    }
                },
                cacheResolver = object : CacheKeyResolver() {
                    override fun cacheKeyForField(
                        field: CompiledField,
                        variables: Executable.Variables
                    ): CacheKey? {
                        val typename = field.type.leafType().name

                        if(typename == "Repository") {
                            val owner = field.resolveArgument("owner", variables)
                            val name = field.resolveArgument("name", variables)
                            if (owner is String && name is String) {
                                return CacheKey(typename, owner, name)
                            }
                        }

                        val id = field.resolveArgument("id", variables)
                        if (id is String) {
                            return CacheKey(typename, id)
                        }

                        return null
                    }
                }
            )
            .addHttpInterceptor(
                object : HttpInterceptor {
                    override suspend fun intercept(
                        request: HttpRequest,
                        chain: HttpInterceptorChain
                    ): HttpResponse {
                        return chain.proceed(
                            request.newBuilder().addHeader(
                                "Authorization",
                                "Bearer ${BuildConfig.GIT_TOKEN}"
                            ).build()
                        )
                    }
                }
            ).build()
    }
}
