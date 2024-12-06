package com.softcross.ecommercecompose.presentation.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsContract.UiState())
    val uiState: StateFlow<ProductsContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<ProductsContract.UiEffect>() }
    val uiEffect: Flow<ProductsContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        savedStateHandle.get<String>("categoryID")?.let {
            searchNewProduct(it, _uiState.value.currentPage)
            updateUiState { copy(categoryID = it)}
        }
    }

    fun onAction(uiAction: ProductsContract.UiAction) {
        when (uiAction) {
            is ProductsContract.UiAction.OnReachEndOfList -> {
                updateUiState { copy(currentPage = _uiState.value.currentPage + 1) }
                searchNextPage(_uiState.value.categoryID, _uiState.value.currentPage)
            }
        }
    }


    private fun searchNewProduct(categoryID: String, page: Int) = viewModelScope.launch {
        when (val response = productRepository.getCategoryProducts(categoryID, page)) {
            is ResponseState.Error -> updateUiState {
                copy(
                    isError = true,
                    isLoading = false,
                    errorMessage = response.exception.message ?: "An error occurred"
                )
            }

            is ResponseState.Loading -> updateUiState {
                copy(isLoading = true, isError = false)
            }

            is ResponseState.Success -> {
                println(response)
                updateUiState {
                    if (response.result.products.isEmpty()) {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = "Herhangi bir ürün bulunamadı"
                        )
                    } else {
                        copy(
                            isLoading = false,
                            products = response.result.products,
                            totalItems = response.result.totalItem,
                            totalPages = response.result.totalPage,
                            categoryName = response.result.categoryName
                        )
                    }
                }
            }
        }
    }

    private fun searchNextPage(categoryID: String, page: Int) = viewModelScope.launch {
        if (page <= _uiState.value.totalPages && !_uiState.value.isMoreLoading) {
            when (val response = productRepository.getCategoryProducts(categoryID, page)) {
                is ResponseState.Error -> updateUiState {
                    copy(
                        isError = true,
                        isMoreLoading = false,
                        errorMessage = response.exception.message ?: "An error occurred"
                    )
                }

                is ResponseState.Loading -> updateUiState {
                    copy(isMoreLoading = true, isError = false)
                }

                is ResponseState.Success -> {
                    println(response)
                    updateUiState {
                        if (response.result.products.isEmpty()) {
                            copy(
                                isMoreLoading = false,
                                isError = true,
                                errorMessage = "Herhangi bir ürün bulunamadı"
                            )
                        } else {
                            copy(
                                isMoreLoading = false,
                                products = _uiState.value.products + response.result.products,
                                totalItems = response.result.totalItem,
                                totalPages = response.result.totalPage
                            )
                        }
                    }
                }
            }
        }
    }


    private fun updateUiState(block: ProductsContract.UiState.() -> ProductsContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: ProductsContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}