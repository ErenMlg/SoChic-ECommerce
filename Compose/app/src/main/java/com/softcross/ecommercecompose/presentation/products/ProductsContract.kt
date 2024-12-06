package com.softcross.ecommercecompose.presentation.products

import com.softcross.ecommercecompose.data.model.product.Product

object ProductsContract {
    data class UiState(
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val errorMessage:String = "",
        val totalItems:Int = 0,
        val totalPages:Int = 0,
        val currentPage:Int = 1,
        val isMoreLoading:Boolean = false,
        val categoryName:String="",
        val categoryID:String=""
    )

    sealed class UiAction {
        data object OnReachEndOfList : UiAction()
    }

    sealed class UiEffect {
    }
}