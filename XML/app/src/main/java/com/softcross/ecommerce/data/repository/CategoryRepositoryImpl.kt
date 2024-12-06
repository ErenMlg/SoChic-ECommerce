package com.softcross.ecommerce.data.repository

import com.softcross.ecommerce.common.ResponseState
import com.softcross.ecommerce.common.extensions.toCategory
import com.softcross.ecommerce.common.extensions.toSubCategory
import com.softcross.ecommerce.data.model.category.MainCategories
import com.softcross.ecommerce.data.model.category.SubCategories
import com.softcross.ecommerce.domain.repository.CategoryRepository
import com.softcross.ecommerce.domain.source.GraphqlDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val graphqlDataSource: GraphqlDataSource) :
    CategoryRepository {

    /*
    Kategorilerin listesini çekiyoruz, gelen listeden filtreleme yapıp
    ürünleri çekmek için gereken kategori idlerini ve alt kategorileri belirliyoruz.
     */
    override suspend fun getCategoryList(): Flow<ResponseState<List<MainCategories>>> {
        return flow {
            emit(ResponseState.Loading)
            val allCategories = fetchAllCategories()
            val mappedCategories = fetchMappedCategories()
            val organizedCategories = organizeCategories(mappedCategories).dropLast(4)
            val updatedCategories = updateSubCategoryIds(allCategories, organizedCategories)
            updatedCategories.forEach {
                if (!it.subCategories.isNullOrEmpty()) {
                    it.subCategories.add(
                        0,
                        SubCategories(id = it.id.toString(), name = "Tümünü Gör")
                    )
                }
            }
            emit(ResponseState.Success(updatedCategories))
        }.catch {
            emit(ResponseState.Error(Exception(it.message)))
        }
    }

    // Tüm kategorileri çekiyoruz.
    private suspend fun fetchAllCategories(): List<SubCategories> {
        val allCategories = graphqlDataSource.queryAllCategories().data?.categories?.map {
            it.toCategory()
        } ?: emptyList()
        return allCategories
    }

    // Türü Main Menü olan kategorileri çekiyoruz
    private suspend fun fetchMappedCategories(): List<MainCategories> {
        val response = graphqlDataSource.queryCategoryList()
        return response.data?.menuByType?.map { it.toCategory() } ?: emptyList()
    }

    // Kategorileri organize ediyoruz. Ana kategori mi alt kategori mi belirleyip yerleştiriyoruz.
    private fun organizeCategories(categories: List<MainCategories>): List<MainCategories> {

        // Ana kategoriyi buluyoruz.
        fun findMainParent(parentID: Int?): MainCategories? {
            var current = categories.find { it.id == parentID }
            while (current?.parentMenuId != null) {
                current = categories.find { it.id == current?.parentMenuId }
            }
            return current
        }

        // Kategorinin alt kategorisi olup olmadığını kontrol ediyoruz.
        fun isIntermediateCategory(categoryId: Int): Boolean =
            categories.any { it.parentMenuId == categoryId }

        val groupedCategories = categories.filter { it.parentMenuId == null }.toMutableList()

        categories.filter { it.parentMenuId != null }.forEach { category ->
            val mainParent = findMainParent(category.parentMenuId)
            if (mainParent != null &&
                !isIntermediateCategory(category.id) &&
                category.type != "QuadrupleBanner" &&
                category.name?.contains("Image Banner") != true
            ) {
                groupedCategories.find { it.id == mainParent.id }?.subCategories?.add(category.toSubCategory())
            }
        }

        return groupedCategories
    }

    // Kategorilerin alt kategorilerinin ID'lerini tüm kategorilere göre güncelliyoruz.
    private fun updateSubCategoryIds(
        allCategories: List<SubCategories>,
        mainCategories: List<MainCategories>
    ): List<MainCategories> {

        // Tüm kategorilerden main menü türündeki kategorilerin idlerini arayıp buluyoruz ve güncelliyoruz.
        fun findMatchingCategory(
            subCategory: SubCategories,
            allCategories: List<SubCategories>
        ): SubCategories? {
            val subCategoryName = subCategory.name ?: return null

            val keywords = subCategoryName.split(" ").map { it.trim().lowercase() }

            val exactMatch =
                allCategories.find { it.name?.lowercase() == subCategoryName.lowercase() }
            if (exactMatch != null) return exactMatch

            return allCategories.find { allCategory ->
                val allCategoryName = allCategory.name?.lowercase() ?: ""
                keywords.all { keyword -> allCategoryName.contains(keyword) }
            }
        }

        return mainCategories.map { mainCategory ->
            val updatedMainCategoryID = allCategories.find {
                it.name.equals(mainCategory.name, ignoreCase = true) &&
                        it.parentMenuId.isNullOrEmpty()
            }?.id ?: allCategories.find {
                it.name?.contains(mainCategory.name ?: "", ignoreCase = true) == true &&
                        it.parentMenuId.isNullOrEmpty()
            }?.id
            val updatedSubCategories = mainCategory.subCategories?.map { subCategory ->
                val realSubCategory = findMatchingCategory(subCategory, allCategories)
                subCategory.copy(
                    id = realSubCategory?.id ?: subCategory.id,
                    parentMenuId = realSubCategory?.parentMenuId
                )
            }
            mainCategory.copy(
                id = updatedMainCategoryID?.toInt() ?: mainCategory.id,
                subCategories = updatedSubCategories?.toMutableList()
            )
        }
    }

}
