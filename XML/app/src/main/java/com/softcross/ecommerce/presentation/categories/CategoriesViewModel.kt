package com.softcross.ecommerce.presentation.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.common.SoChicSingleton
import com.softcross.ecommerce.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categoriesUiState = MutableLiveData<CategoriesUiState>()
    val categoriesUiState: LiveData<CategoriesUiState> get() = _categoriesUiState

    init {
        // Eğer singleton'da kategori listesi varsa onu kullan, yoksa kategorileri çek.
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
            _categoriesUiState.postValue(
                CategoriesUiState(
                    categories = categories,
                    isLoading = false
                )
            )
        }
    }

    // Kategorileri çekmek için oluşturduğumuz fonksiyon
    private fun getCategories() = viewModelScope.launch {
        getCategoriesUseCase().collect { responseState ->
            when (responseState) {
                is ResponseState.Error -> {
                    _categoriesUiState.postValue(
                        CategoriesUiState(
                            isError = true,
                            isLoading = false,
                            errorMessage = responseState.exception.message ?: "An error occurred"
                        )
                    )
                }

                ResponseState.Loading -> {
                    _categoriesUiState.postValue(
                        CategoriesUiState(
                            isLoading = true
                        )
                    )
                }

                is ResponseState.Success -> {
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
                    _categoriesUiState.postValue(
                        if (categories.isEmpty()) {
                            CategoriesUiState(
                                isError = true,
                                errorMessage = "No categories found",
                                isLoading = false
                            )
                        } else {
                            CategoriesUiState(categories = categories, isLoading = false)
                        }
                    )
                }
            }
        }
    }

}

// Kategorilerin durumunu tutan sınıf, loading, error ve kategorilerin listesini tutar
data class CategoriesUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val categories: List<CategoryItem> = emptyList()
)

// Kategori item'ı, alt kategorileri varsa alt kategorileri de tutar
data class CategoryItem(
    val id: String,
    val name: String,
    val image: String = "",
    val type: CategoryType = CategoryType.PARENT,
    var isExpanded: Boolean = false,
    val subItems: List<CategoryItem>? = null
)

// Kategori tipi, parent ve child olmak üzere iki tipi vardır
enum class CategoryType {
    PARENT,
    CHILD
}