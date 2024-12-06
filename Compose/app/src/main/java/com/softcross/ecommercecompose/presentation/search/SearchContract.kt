package com.softcross.ecommercecompose.presentation.search

import com.softcross.ecommercecompose.data.model.product.Product

object SearchContract {

    data class SearchState(
        val searchQuery: String = "",
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = true,
        val errorMessage:String = "Henüz bir arama yapmadınız...",
        val totalItems:Int = 0,
        val totalPages:Int = 0,
        val currentPage:Int = 1,
        val isMoreLoading:Boolean = false
    )

    sealed class SearchAction {
        data class OnSearchQueryChanged(val query: String) : SearchAction()
        data object OnSearchDone : SearchAction()
        data object OnReachEndOfList : SearchAction()
    }

    sealed class SearchEffect {
    }

}