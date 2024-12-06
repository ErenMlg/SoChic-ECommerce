package com.softcross.ecommercecompose.presentation.categories

import com.softcross.ecommercecompose.presentation.home.HomeItem

object CategoriesContract {
    data class UiState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val categoryList: List<CategoryItem> = emptyList()
    )

    sealed class UiAction {
        data class OnCategoryClick(val categoryID: String) : UiAction()
    }

    sealed class UiEffect {
    }
}