package com.softcross.ecommercecompose.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.domain.repository.ProductRepository
import com.softcross.ecommercecompose.presentation.search.SearchContract.SearchAction
import com.softcross.ecommercecompose.presentation.search.SearchContract.SearchEffect
import com.softcross.ecommercecompose.presentation.search.SearchContract.SearchState
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
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<SearchEffect>() }
    val uiEffect: Flow<SearchEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: SearchAction) {
        when (uiAction) {
            is SearchAction.OnSearchDone -> searchNewProduct(
                _uiState.value.searchQuery,
                _uiState.value.currentPage
            )

            is SearchAction.OnSearchQueryChanged -> updateUiState { copy(searchQuery = uiAction.query) }
            is SearchAction.OnReachEndOfList -> {
                updateUiState { copy(currentPage = _uiState.value.currentPage + 1) }
                searchNextPage(_uiState.value.searchQuery, _uiState.value.currentPage)
            }
        }
    }

    private fun searchNewProduct(string: String, page: Int) = viewModelScope.launch {
        productRepository.searchProduct(string, page).collect { response ->
            when (response) {
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
                                totalPages = response.result.totalPage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun searchNextPage(string: String, page: Int) = viewModelScope.launch {
        if (page <= _uiState.value.totalPages && !_uiState.value.isMoreLoading) {
            productRepository.searchProduct(string, page).collect { response ->
                when (response) {
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
    }

    private fun updateUiState(block: SearchState.() -> SearchState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: SearchEffect) {
        _uiEffect.send(uiEffect)
    }

}