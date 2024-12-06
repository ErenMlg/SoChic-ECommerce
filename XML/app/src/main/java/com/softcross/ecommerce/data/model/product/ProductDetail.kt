package com.softcross.ecommerce.data.model.product

data class ProductDetail(
    val id: String,
    val name: String,
    val fullDescription: String,
    val price: String,
    val discountedPrice: String,
    val images: List<String>,
    val isFreeShipping: Boolean,
    val variants: List<Variant>,
)

data class VariantItem(
    val id: String,
    val name: String,
    val inStock: Boolean
)

data class Variant(
    val name: String,
    val items: List<VariantItem>
)

