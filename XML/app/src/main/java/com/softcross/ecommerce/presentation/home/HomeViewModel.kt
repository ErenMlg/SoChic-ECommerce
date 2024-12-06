package com.softcross.ecommerce.presentation.home

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
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _homeScreenUiState = MutableLiveData<HomeScreenUiState>()
    val homeScreenUiState: LiveData<HomeScreenUiState> get() = _homeScreenUiState

    init {
        // Eğer Singleton'da kategori listesi varsa, onu kullan. Yoksa API'den çek.
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
            _homeScreenUiState.postValue(
                HomeScreenUiState(
                    categories = categories
                )
            )
        }
    }

    // Kategorileri çekmek için kullanılan fonksiyon
    private fun getCategories() = viewModelScope.launch {
        getCategoriesUseCase().collect { responseState ->
            when (responseState) {
                is ResponseState.Error -> {
                    _homeScreenUiState.postValue(
                        HomeScreenUiState(
                            isError = true,
                            isLoading = false,
                            errorMessage = responseState.exception.message ?: "An error occurred"
                        )
                    )
                }

                ResponseState.Loading -> {
                    _homeScreenUiState.postValue(
                        HomeScreenUiState(
                            isLoading = true
                        )
                    )
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
                    _homeScreenUiState.postValue(
                        if (categories.isEmpty()) {
                            HomeScreenUiState(
                                isError = true,
                                isLoading = false,
                                errorMessage = "No data found"
                            )
                        } else {
                            HomeScreenUiState(categories = categories, isLoading = false)
                        }
                    )
                }
            }
        }
    }


}

data class HomeItem(
    val id: Int,
    val name: String,
    val imageUrl: String
)

data class HomeScreenUiState(
    val categories: List<HomeItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)