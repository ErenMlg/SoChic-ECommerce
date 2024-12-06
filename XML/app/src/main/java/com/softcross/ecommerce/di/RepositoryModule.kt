package com.softcross.ecommerce.di

import com.softcross.ecommerce.data.repository.CategoryRepositoryImpl
import com.softcross.ecommerce.data.repository.ProductRepositoryImpl
import com.softcross.ecommerce.domain.repository.CategoryRepository
import com.softcross.ecommerce.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/*
 * DI modülümüz sadece ViewModel tarafında kullanılacağı için ViewModelComponent içerisinde tanımlıyoruz.
 */
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