package com.softcross.ecommerce.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.product.Product
import com.softcross.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchUiState = MutableLiveData(SearchUiState.initial())
    val searchUiState: LiveData<SearchUiState> get() = _searchUiState

    fun searchProduct(string: String, page: Int) = viewModelScope.launch {
        Log.e("SearchViewModel", "searchProduct: $string")
        productRepository.searchProduct(string, page).collect { response ->
            when (response) {
                is ResponseState.Error -> _searchUiState.postValue(
                    SearchUiState(
                        isError = true,
                        isLoading = false,
                        errorMessage = response.exception.message ?: "An error occurred"
                    )
                )

                is ResponseState.Loading ->
                    _searchUiState.postValue(SearchUiState(isLoading = true))

                is ResponseState.Success -> {
                    _searchUiState.postValue(
                        if (response.result.products.isEmpty()) {
                            SearchUiState(
                                isLoading = false,
                                isError = true,
                                errorMessage = "Herhangi bir ürün bulunamadı"
                            )
                        } else {
                            SearchUiState(
                                isLoading = false,
                                products = response.result.products,
                                totalItems = response.result.totalItem,
                                totalPages = response.result.totalPage
                            )
                        }
                    )
                }
            }
        }
    }
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val errorMessage: String = "",
    val products: List<Product> = emptyList()
) {
    companion object {
        fun initial() = SearchUiState(isError = true, errorMessage = "Lütfen bir arama yapın...")
    }
}