package com.softcross.ecommercecompose.common

import com.softcross.ecommercecompose.data.model.category.MainCategories

object SoChicSingleton {

    private var categoryList: List<MainCategories>? = null

    fun setCategoryList(categories: List<MainCategories>) {
        categoryList = categories
    }

    fun clearCategoryList() {
        categoryList = null
    }

    fun getCategoryList(): List<MainCategories>? {
        return categoryList
    }

    fun isCategoryListNull(): Boolean {
        return categoryList == null
    }

}