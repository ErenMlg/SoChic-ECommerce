package com.softcross.ecommercecompose.data.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.softcross.ecommerce.AllCategoriesQuery
import com.softcross.ecommerce.CategoryListQuery
import com.softcross.ecommerce.CategoryProductsQuery
import com.softcross.ecommerce.ProductDetailQuery
import com.softcross.ecommerce.SearchProductQuery
import com.softcross.ecommercecompose.domain.source.GraphqlDataSource
import javax.inject.Inject

class GraphqlDataSourceImpl @Inject constructor(private val apolloClient: ApolloClient) :
    GraphqlDataSource {

    override suspend fun queryCategoryList(): ApolloResponse<CategoryListQuery.Data> {
        val result = apolloClient.query(CategoryListQuery()).execute()
        println(result.data)
        println(result.hasErrors())
        println(result.exception)
        return result
    }

    override suspend fun queryCategoryProducts(
        categoryId: String,
        page: Int
    ): ApolloResponse<CategoryProductsQuery.Data> {
        val result = apolloClient.query(CategoryProductsQuery(categoryId, page)).execute()
        println(result.data)
        println(result.hasErrors())
        println(result.exception)
        return result
    }

    override suspend fun queryAllCategories(): ApolloResponse<AllCategoriesQuery.Data> {
        val result =  apolloClient.query(AllCategoriesQuery()).execute()
        println(result.data)
        println(result.hasErrors())
        println(result.exception)
        return result
    }

    override suspend fun queryProductDetail(productId: String): ApolloResponse<ProductDetailQuery.Data> {
        val result =  apolloClient.query(ProductDetailQuery(productId)).execute()
        println(result.data)
        println(result.hasErrors())
        println(result.exception)
        return result
    }

    override suspend fun querySearchProduct(
        search: String,
        page: Int
    ): ApolloResponse<SearchProductQuery.Data> {
        val result =  apolloClient.query(SearchProductQuery(search, page)).execute()
        println(result.data)
        println(result.hasErrors())
        println(result.exception)
        return result
    }


}