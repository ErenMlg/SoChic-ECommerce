package com.softcross.ecommerce.data.model.category

data class MainCategories(
    val id: Int,
    val name: String? = null,
    val icon: String? = null,
    val parentMenuId: Int? = null,
    val type: String? = null,
    val subCategories: MutableList<SubCategories>? = mutableListOf()
)

