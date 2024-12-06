package com.softcross.ecommerce.di

import com.softcross.ecommerce.data.network.GraphqlDataSourceImpl
import com.softcross.ecommerce.domain.source.GraphqlDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

/*
 * DI modülümüz sadece ViewModel tarafında kullanılacağı için ViewModelComponent içerisinde tanımlıyoruz.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class SourceModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGraphqlDataSource(graphqlDataSource: GraphqlDataSourceImpl): GraphqlDataSource

}