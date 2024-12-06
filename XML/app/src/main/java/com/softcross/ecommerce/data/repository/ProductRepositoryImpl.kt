package com.softcross.ecommerce.data.repository

import android.util.Log
import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.common.extensions.toProduct
import com.softcross.ecommerce.common.extensions.toProductDetail
import com.softcross.ecommerce.data.model.SearchResponse
import com.softcross.ecommerce.data.model.category.CategoryProductsResponse
import com.softcross.ecommerce.data.model.product.ProductDetail
import com.softcross.ecommerce.domain.repository.ProductRepository
import com.softcross.ecommerce.domain.source.GraphqlDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val graphqlDataSource: GraphqlDataSource) :
    ProductRepository {

        //Bir kategori türüne ait ürünleri getirir.
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

    //Ürün detaylarını getirir.
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

    //Arama yaparak ürünleri getirir.
    override suspend fun searchProduct(
        search: String,
        page: Int
    ): Flow<ResponseState<SearchResponse>> {
        return flow {
            emit(ResponseState.Loading)
            val response = graphqlDataSource.querySearchProduct(search, page).data?.searchV2?.data
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