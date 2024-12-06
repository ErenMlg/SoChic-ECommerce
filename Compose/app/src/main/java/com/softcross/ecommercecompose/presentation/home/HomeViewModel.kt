package com.softcross.ecommercecompose.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.common.SoChicSingleton
import com.softcross.ecommercecompose.domain.usecase.GetCategoriesUseCase
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
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.UiState())
    val uiState: StateFlow<HomeContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<HomeContract.UiEffect>() }
    val uiEffect: Flow<HomeContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        val singletonCategoryList = SoChicSingleton.getCategoryList()
        if (singletonCategoryList == null) {
            getCategories()
        } else {
            val categories = singletonCategoryList.map {
                HomeItem(
                    id = it.id,
                    name = it.name ?: "Category",
                    imageUrl = it.icon?.substringAfter("MenuItem/")?.lowercase() ?: "kolye.png"
                )
            }
            updateUiState {
                copy(
                    categoryList = categories
                )
            }
        }
    }

    fun onAction(uiAction: HomeContract.UiAction) {
        when(uiAction){
            is HomeContract.UiAction.OnCategoryClick -> {}
            HomeContract.UiAction.OnSeeAllClick -> {}
        }
    }

    private fun getCategories() = viewModelScope.launch {
        getCategoriesUseCase().collect { responseState ->
            when (responseState) {
                is ResponseState.Error -> {
                    updateUiState {
                        copy(
                            isError = true,
                            isLoading = false,
                            errorMessage = responseState.exception.message ?: "An error occurred"
                        )
                    }
                }

                ResponseState.Loading -> {
                    updateUiState{
                        copy(
                            isLoading = true
                        )
                    }
                }

                is ResponseState.Success -> {
                    SoChicSingleton.setCategoryList(responseState.result)
                    val categories = responseState.result.map {
                        HomeItem(
                            id = it.id,
                            name = it.name ?: "Category",
                            imageUrl = it.icon?.substringAfter("MenuItem/")?.lowercase()
                                ?: "kolye.png"
                        )
                    }
                    updateUiState {
                        if (categories.isEmpty()) {
                            copy(
                                isError = true,
                                isLoading = false,
                                errorMessage = "No data found"
                            )
                        } else {
                            copy(categoryList = categories, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun updateUiState(block: HomeContract.UiState.() -> HomeContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: HomeContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}

data class HomeItem(
    val id: Int,
    val name: String,
    val imageUrl: String
)