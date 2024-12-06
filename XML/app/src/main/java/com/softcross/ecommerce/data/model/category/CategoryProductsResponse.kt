package com.softcross.ecommerce.data.model.category

import com.softcross.ecommerce.data.model.product.Product

data class CategoryProductsResponse(
    val categoryID: String,
    val categoryName: String,
    val totalItem: Int,
    val totalPage: Int,
    val products: List<Product>
)