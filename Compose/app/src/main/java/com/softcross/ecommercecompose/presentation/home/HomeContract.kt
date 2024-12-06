package com.softcross.ecommercecompose.presentation.home

object HomeContract {
    data class UiState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val categoryList: List<HomeItem> = emptyList()
    )

    sealed class UiAction {
        data class OnCategoryClick(val categoryID: String) : UiAction()
        data object OnSeeAllClick : UiAction()
    }

    sealed class UiEffect {
    }
}