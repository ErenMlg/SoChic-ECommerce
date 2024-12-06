package com.softcross.ecommercecompose.di

import com.softcross.ecommercecompose.data.repository.CategoryRepositoryImpl
import com.softcross.ecommercecompose.data.repository.ProductRepositoryImpl
import com.softcross.ecommercecompose.domain.repository.CategoryRepository
import com.softcross.ecommercecompose.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @ViewModelScoped
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

}