package com.softcross.ecommercecompose.common.extensions

import com.softcross.ecommerce.AllCategoriesQuery
import com.softcross.ecommerce.CategoryListQuery
import com.softcross.ecommerce.CategoryProductsQuery
import com.softcross.ecommerce.ProductDetailQuery
import com.softcross.ecommerce.SearchProductQuery
import com.softcross.ecommercecompose.data.model.category.MainCategories
import com.softcross.ecommercecompose.data.model.category.SubCategories
import com.softcross.ecommercecompose.data.model.product.Product
import com.softcross.ecommercecompose.data.model.product.ProductDetail
import com.softcross.ecommercecompose.data.model.product.Variant
import com.softcross.ecommercecompose.data.model.product.VariantItem

fun (CategoryListQuery.MenuByType)?.toCategory(): MainCategories = MainCategories(
    id = this?.id ?: -1,
    name = this?.name,
    icon = this?.icon,
    parentMenuId = this?.parentMenuId,
    type = this?.type
)

fun MainCategories.toSubCategory(): SubCategories = SubCategories(
    id = this.id.toString(),
    name = this.name,
    parentMenuId = this.parentMenuId.toString()
)

fun (AllCategoriesQuery.Category)?.toCategory(): SubCategories = SubCategories(
    id = this?.id.toString(),
    name = this?.name,
    parentMenuId = this?.parentCategoryId,
)

fun (ProductDetailQuery.Product)?.toProductDetail(): ProductDetail = ProductDetail(
    id = this?.id.toString(),
    name = this?.name ?: "",
    fullDescription = this?.fullDescription ?: "",
    price = this?.productPrice?.oldPrice ?: "",
    discountedPrice = this?.productPrice?.price ?: "",
    images = this?.pictureModels?.map { it?.imageUrl ?: "" } ?: emptyList(),
    isFreeShipping = this?.isFreeShipping ?: false,
    variants = parseVariants(this?.productSlicers)

)

fun (CategoryProductsQuery.Product)?.toProduct(): Product = Product(
    id = this?.id ?: "",
    name = this?.name ?: "",
    shortDescription = this?.shortDescription ?: "",
    price = this?.price?.oldPrice ?: "",
    discountedPrice = this?.price?.price ?: "",
    images = this?.pictures?.map { it?.imageUrl ?: "" } ?: emptyList()
)

fun (SearchProductQuery.Product)?.toProduct(): Product = Product(
    id = this?.id ?: "",
    name = this?.name ?: "",
    shortDescription = this?.shortDescription ?: "",
    price = this?.price?.oldPrice ?: "",
    discountedPrice = this?.price?.price ?: "",
    images = this?.pictures?.map { it?.imageUrl ?: "" } ?: emptyList()
)

private fun parseVariants(productSlicers: ProductDetailQuery.ProductSlicers?): List<Variant> {

    val groupedVariants = productSlicers?.variants?.groupBy { variant ->
        variant?.slicerAttributes?.firstOrNull()?.attributeName ?: ""
    } ?: emptyMap()

    return groupedVariants.map { (attributeName, variants) ->
        Variant(
            name = attributeName,
            items = variants.filterNotNull().map {
                VariantItem(
                    id = it.productId ?: "",
                    name = it.slicerAttributes?.firstOrNull()?.attributeValue ?: "",
                    inStock = it.inStock
                )
            }
        )
    }
}
