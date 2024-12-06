package com.softcross.ecommercecompose.data.repository

import android.util.Log
import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.common.extensions.toProduct
import com.softcross.ecommercecompose.common.extensions.toProductDetail
import com.softcross.ecommercecompose.data.model.SearchResponse
import com.softcross.ecommercecompose.data.model.category.CategoryProductsResponse
import com.softcross.ecommercecompose.data.model.product.ProductDetail
import com.softcross.ecommercecompose.domain.repository.ProductRepository
import com.softcross.ecommercecompose.domain.source.GraphqlDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val graphqlDataSource: GraphqlDataSource) :
    ProductRepository {

    override suspend fun getCategoryProducts(
        categoryId: String,
        page: Int
    ): ResponseState<CategoryProductsResponse> {
        return try {
            ResponseState.Loading
            val response =
                graphqlDataSource.queryCategoryProducts(categoryId, page).data?.categoryV2?.data
            ResponseState.Success(CategoryProductsResponse(
                categoryID = categoryId,
                categoryName = response?.name ?: "",
                totalItem = response?.totalItems ?: 0,
                totalPage = response?.totalPages ?: 0,
                products = response?.products?.map { it.toProduct() } ?: emptyList()
            ))
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun getProductDetails(productId: String): ResponseState<ProductDetail> {
        return try {
            ResponseState.Loading
            val response = graphqlDataSource.queryProductDetail(productId).data?.product
                ?: return ResponseState.Error(Exception("Product not found"))
            ResponseState.Success(response.toProductDetail())
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun searchProduct(
        search: String,
        page: Int
    ): Flow<ResponseState<SearchResponse>> {
        return flow {
            Log.e("SearchViewModel", "searchProduct: $search")
            emit(ResponseState.Loading)
            val response = graphqlDataSource.querySearchProduct(search, page).data?.searchV2?.data
            Log.e("SearchViewModel", "response: $response")
            if (response == null) {
                emit(ResponseState.Error(Exception("No data found")))
            } else {
                emit(ResponseState.Success(SearchResponse(
                    totalItem = response.totalItems ?: 0,
                    totalPage = response.totalPages ?: 0,
                    products = response.products?.map { it.toProduct() } ?: emptyList()
                )))
            }
        }.catch {
            ResponseState.Error(Exception(it.message))
        }
    }

}