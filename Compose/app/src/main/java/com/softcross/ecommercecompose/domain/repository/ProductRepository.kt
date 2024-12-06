package com.softcross.ecommercecompose.domain.repository

import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.data.model.SearchResponse
import com.softcross.ecommercecompose.data.model.category.CategoryProductsResponse
import com.softcross.ecommercecompose.data.model.product.ProductDetail
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getCategoryProducts(
        categoryId: String,
        page: Int
    ): ResponseState<CategoryProductsResponse>

    suspend fun getProductDetails(productId: String): ResponseState<ProductDetail>

    suspend fun searchProduct(search: String, page: Int): Flow<ResponseState<SearchResponse>>

}