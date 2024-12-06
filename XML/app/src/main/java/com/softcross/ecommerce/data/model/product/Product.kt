package com.softcross.ecommerce.data.model.product

data class Product(
    val id: String,
    val name: String,
    val shortDescription: String,
    val price: String,
    val discountedPrice: String,
    val images: List<String>
)


