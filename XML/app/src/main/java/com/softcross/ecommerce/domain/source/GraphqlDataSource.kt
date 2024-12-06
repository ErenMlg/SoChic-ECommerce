package com.softcross.ecommerce.domain.source

import com.apollographql.apollo.api.ApolloResponse
import com.softcross.ecommerce.AllCategoriesQuery
import com.softcross.ecommerce.CategoryListQuery
import com.softcross.ecommerce.CategoryProductsQuery
import com.softcross.ecommerce.ProductDetailQuery
import com.softcross.ecommerce.SearchProductQuery

interface GraphqlDataSource {

    suspend fun queryCategoryList():ApolloResponse<CategoryListQuery.Data>

    suspend fun queryCategoryProducts(categoryId: String, page:Int): ApolloResponse<CategoryProductsQuery.Data>

    suspend fun queryAllCategories(): ApolloResponse<AllCategoriesQuery.Data>

    suspend fun queryProductDetail(productId: String): ApolloResponse<ProductDetailQuery.Data>

    suspend fun querySearchProduct(search: String, page: Int): ApolloResponse<SearchProductQuery.Data>

}