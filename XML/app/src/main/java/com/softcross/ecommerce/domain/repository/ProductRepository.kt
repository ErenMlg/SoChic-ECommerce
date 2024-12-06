package com.softcross.ecommerce.domain.repository

import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.SearchResponse
import com.softcross.ecommerce.data.model.category.CategoryProductsResponse
import com.softcross.ecommerce.data.model.product.ProductDetail
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getCategoryProducts(
        categoryId: String,
        page: Int
    ): ResponseState<CategoryProductsResponse>

    suspend fun getProductDetails(productId: String): ResponseState<ProductDetail>

    suspend fun searchProduct(search: String, page: Int): Flow<ResponseState<SearchResponse>>

}