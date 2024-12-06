package com.softcross.ecommercecompose.presentation.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailContract.UiState())
    val uiState: StateFlow<DetailContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<DetailContract.UiEffect>() }
    val uiEffect: Flow<DetailContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        savedStateHandle.get<String>("productID")?.let {
            getProductDetail(it)
            println("INIT İÇİNDE $it")
        }
        println("INIT DIŞINDA")
    }

    fun onAction(uiAction: DetailContract.UiAction) {
        when (uiAction) {
            is DetailContract.UiAction.OnVariantSelected -> {
                getProductDetail(uiAction.variantId)
            }
        }
    }

    private fun getProductDetail(productId: String) = viewModelScope.launch {
        when (val response = productRepository.getProductDetails(productId)) {
            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isError = true,
                        isLoading = false,
                        errorMessage = response.exception.message ?: "Unknown error"
                    )
                }
            }

            is ResponseState.Loading -> {
                updateUiState { (copy(isLoading = true)) }
            }

            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        product = response.result,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateUiState(block: DetailContract.UiState.() -> DetailContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: DetailContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}