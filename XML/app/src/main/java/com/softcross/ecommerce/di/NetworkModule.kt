package com.softcross.ecommerce.di

import com.apollographql.apollo.ApolloClient
import com.softcross.ecommerce.BuildConfig
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/*
 * DI modülümüz sadece ViewModel tarafında kullanılacağı için ViewModelComponent içerisinde tanımlıyoruz.
 */
@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    // ENDPOINT ve diğer header bilgilerini ekleyerek ApolloClient'ı oluşturuyoruz.
    // Endpoint bilgisini local.properties dosyasından almak için BuildConfig.API_ENDPOINT şeklinde kullanıyoruz.
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