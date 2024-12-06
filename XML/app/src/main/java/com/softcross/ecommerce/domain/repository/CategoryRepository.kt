package com.softcross.ecommerce.domain.repository

import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.data.model.category.MainCategories
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategoryList(): Flow<ResponseState<List<MainCategories>>>

}