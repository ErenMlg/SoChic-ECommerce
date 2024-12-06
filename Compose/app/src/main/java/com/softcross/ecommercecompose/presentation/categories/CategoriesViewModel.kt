package com.softcross.ecommercecompose.presentation.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.common.SoChicSingleton
import com.softcross.ecommercecompose.domain.usecase.GetCategoriesUseCase
import com.softcross.ecommercecompose.presentation.home.HomeContract
import com.softcross.ecommercecompose.presentation.home.HomeItem
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesContract.UiState())
    val uiState: StateFlow<CategoriesContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<CategoriesContract.UiEffect>() }
    val uiEffect: Flow<CategoriesContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        val singletonCategoryList = SoChicSingleton.getCategoryList()
        if (singletonCategoryList == null) {
            getCategories()
        } else {
            val categories = singletonCategoryList.map {
                CategoryItem(
                    id = it.id.toString(),
                    name = it.name ?: "Unkown Category",
                    image = it.icon?.substringAfter("MenuItem/")?.lowercase() ?: "kolye.png",
                    subItems = it.subCategories?.map { subCategory ->
                        CategoryItem(
                            id = subCategory.id,
                            name = subCategory.name ?: "Unkown SubCategory",
                            type = CategoryType.CHILD
                        )
                    }
                )
            }
            updateUiState {
                copy(
                    categoryList = categories
                )
            }
        }
    }

    fun onAction(uiAction: CategoriesContract.UiAction) {
        when (uiAction) {
            is CategoriesContract.UiAction.OnCategoryClick -> {}
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
                    updateUiState {
                        copy(
                            isLoading = true
                        )
                    }
                }

                is ResponseState.Success -> {
                    SoChicSingleton.setCategoryList(responseState.result)
                    val categories = responseState.result.map {
                        CategoryItem(
                            id = it.id.toString(),
                            name = it.name ?: "Unkown Category",
                            image = it.icon?.substringAfter("MenuItem/")?.lowercase()
                                ?: "kolye.png",
                            subItems = it.subCategories?.map { subCategory ->
                                CategoryItem(
                                    id = subCategory.id,
                                    name = subCategory.name ?: "Unkown SubCategory",
                                    type = CategoryType.CHILD
                                )
                            }
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

    private fun updateUiState(block: CategoriesContract.UiState.() -> CategoriesContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: CategoriesContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}

data class CategoryItem(
    val id: String,
    val name: String,
    val image: String = "",
    val type: CategoryType = CategoryType.PARENT,
    var isExpanded: Boolean = false,
    val subItems: List<CategoryItem>? = null
)

enum class CategoryType {
    PARENT,
    CHILD
}