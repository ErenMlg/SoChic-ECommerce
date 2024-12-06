package com.softcross.ecommercecompose.presentation.detail

import com.softcross.ecommercecompose.data.model.product.ProductDetail

object DetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val product: ProductDetail? = null
    )

    sealed class UiAction {
        data class OnVariantSelected(val variantId: String) : UiAction()
    }

    sealed class UiEffect {

    }
}