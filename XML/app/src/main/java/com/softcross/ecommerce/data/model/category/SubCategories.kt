package com.softcross.ecommerce.data.model.category

data class SubCategories(
    val id: String,
    val name: String? = null,
    val parentMenuId: String? = null,
)
