package com.softcross.ecommercecompose.data.model

import com.softcross.ecommercecompose.data.model.product.Product

data class SearchResponse(
    val totalItem: Int,
    val totalPage: Int,
    val products: List<Product>
)