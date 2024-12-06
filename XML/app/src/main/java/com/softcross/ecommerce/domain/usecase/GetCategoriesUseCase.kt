package com.softcross.ecommerce.domain.usecase

import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.category.MainCategories
import com.softcross.ecommerce.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke() : Flow<ResponseState<List<MainCategories>>>{
        return categoryRepository.getCategoryList()
    }

}