package com.softcross.ecommercecompose.domain.usecase

import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.data.model.category.MainCategories
import com.softcross.ecommercecompose.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke() : Flow<ResponseState<List<MainCategories>>>{
        return categoryRepository.getCategoryList()
    }

}