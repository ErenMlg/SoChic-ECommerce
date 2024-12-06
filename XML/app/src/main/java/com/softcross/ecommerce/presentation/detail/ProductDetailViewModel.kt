package com.softcross.ecommerce.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.product.ProductDetail
import com.softcross.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productUiState = MutableLiveData(ProductDetailUiState.initial())
    val productUiState: LiveData<ProductDetailUiState> get() = _productUiState

    // Ürünlerin detaylarını çeker.
    fun getProductDetail(productId: String) = viewModelScope.launch {
        when (val response = productRepository.getProductDetails(productId)) {
            is ResponseState.Error -> {
                _productUiState.postValue(
                    ProductDetailUiState(
                        isError = true,
                        isLoading = false,
                        errorMessage = response.exception.message ?: "Unknown error"
                    )
                )
            }

            is ResponseState.Loading -> {
                _productUiState.postValue(ProductDetailUiState(isLoading = true))
            }

            is ResponseState.Success -> {
                _productUiState.postValue(
                    ProductDetailUiState(
                        product = response.result,
                        isLoading = false
                    )
                )
            }
        }
    }

}

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val product: ProductDetail? = null
) {
    companion object {
        fun initial() = ProductDetailUiState(isLoading = true)
    }
}
