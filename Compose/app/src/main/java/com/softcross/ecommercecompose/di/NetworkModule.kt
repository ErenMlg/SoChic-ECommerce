package com.softcross.ecommercecompose.di

import com.apollographql.apollo.ApolloClient
import com.softcross.ecommercecompose.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    @ViewModelScoped
    fun provideApolloClient(): ApolloClient {
        return lazy {
            ApolloClient.Builder()
                .addHttpHeader("culture","tr-TR")
                .addHttpHeader("language", "tr")
                .addHttpHeader("Content-Type", "application/json")
                .serverUrl(BuildConfig.API_ENDPOINT)
                .build()
        }.value
    }

}