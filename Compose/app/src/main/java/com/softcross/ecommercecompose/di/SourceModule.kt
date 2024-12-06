package com.softcross.ecommercecompose.di

import com.softcross.ecommercecompose.data.network.GraphqlDataSourceImpl
import com.softcross.ecommercecompose.domain.source.GraphqlDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class SourceModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGraphqlDataSource(graphqlDataSource: GraphqlDataSourceImpl): GraphqlDataSource

}