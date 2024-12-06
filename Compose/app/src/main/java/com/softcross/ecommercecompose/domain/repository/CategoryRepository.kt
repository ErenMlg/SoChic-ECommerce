package com.softcross.ecommercecompose.domain.repository

import com.softcross.ecommercecompose.common.ResponseState
import com.softcross.ecommercecompose.data.model.category.MainCategories
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategoryList(): Flow<ResponseState<List<MainCategories>>>

}