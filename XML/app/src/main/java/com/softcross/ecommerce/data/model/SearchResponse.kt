package com.softcross.ecommerce.data.model

import com.softcross.ecommerce.data.model.product.Product

data class SearchResponse(
    val totalItem: Int,
    val totalPage: Int,
    val products: List<Product>
)