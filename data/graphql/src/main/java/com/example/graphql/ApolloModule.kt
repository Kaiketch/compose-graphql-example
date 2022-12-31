package com.example.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
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
            .serverUrl("https://api.github.com/graphql")
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
