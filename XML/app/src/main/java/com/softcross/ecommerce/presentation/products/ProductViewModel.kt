package com.softcross.ecommerce.presentation.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.product.Product
import com.softcross.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _productUiState = MutableLiveData(ProductUiState.initial())
    val productUiState: LiveData<ProductUiState> get() = _productUiState

    init {
        savedStateHandle.get<String>("categoryID")?.let { categoryId ->
            getProducts(categoryId = categoryId, 1)
        }
    }

    fun getProducts(categoryId: String, page: Int) = viewModelScope.launch {
        Log.e("ProductViewModel", "getProducts: $categoryId")
        when (val response = productRepository.getCategoryProducts(categoryId, page)) {
            is ResponseState.Success -> {
                _productUiState.postValue(
                    if (response.result.products.isEmpty()) {
                        ProductUiState(
                            isLoading = false,
                            isError = response.result.products.isEmpty(),
                            errorMessage = "Any product found"
                        )
                    } else {
                        ProductUiState(
                            isLoading = false,
                            categoryName = response.result.categoryName,
                            totalItems = response.result.totalItem,
                            totalPages = response.result.totalPage,
                            products = response.result.products,
                        )
                    }

                )
            }

            is ResponseState.Error -> {
                _productUiState.postValue(
                    ProductUiState(
                        isLoading = false,
                        isError = true,
                        errorMessage = response.exception.message ?: "An error occurred"
                    )
                )
            }

            ResponseState.Loading -> {
                _productUiState.postValue(
                    ProductUiState(
                        isLoading = true
                    )
                )
            }
        }

    }

}

data class ProductUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val categoryName: String = "",
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val products: List<Product> = emptyList()
) {
    companion object {
        fun initial() = ProductUiState(isLoading = true)
    }
}