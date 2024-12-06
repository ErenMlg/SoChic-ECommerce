package com.softcross.ecommerce.presentation.categories

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.databinding.ItemMainCategoryBinding
import com.softcross.ecommerce.databinding.ItemSubCategoryBinding

class CategoriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*
     Kategoriler adapteri burada ana kategoriler ve tıklandığında açılacak alt kategorileri gözükmesini sağlayacız,
     bu yüzden 2 adet view tipimiz var eğer itema tıklanırsa o itemin alt kategorilerini visible listemize ekleyip
     gözükmesini sağlayacağız.
     */

    private var onCategoryClick: ((String) -> Unit)? = null
    private val categoryList = mutableListOf<CategoryItem>()
    private val visibleCategoriesList = mutableListOf<CategoryItem>()

    fun setOnCategoryClick(onCategoryClick: (String) -> Unit) {
        this.onCategoryClick = onCategoryClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(list: List<CategoryItem>) {
        categoryList.clear()
        categoryList.addAll(list)
        updateVisibleCategories()
        notifyDataSetChanged()
    }

    private fun updateVisibleCategories() {
        visibleCategoriesList.clear()
        for (category in categoryList) {
            visibleCategoriesList.add(category)
            if (category.isExpanded) {
                category.subItems?.let { visibleCategoriesList.addAll(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (visibleCategoriesList[position].type == CategoryType.CHILD) {
            SUB_CATEGORY_VIEW_TYPE
        } else {
            MAIN_CATEGORY_VIEW_TYPE
        }
    }

    private fun expandCategory(position: Int, category: CategoryItem) {
        if (category.isExpanded) return
        category.isExpanded = true

        val subCategories = category.subItems ?: return
        val insertPosition = position + 1
        visibleCategoriesList.addAll(insertPosition, subCategories)
        notifyItemRangeInserted(insertPosition, subCategories.size)
    }

    private fun collapseCategory(position: Int, category: CategoryItem) {
        if (!category.isExpanded) return
        category.isExpanded = false

        val subCategories = category.subItems ?: return
        val removePosition = position + 1
        visibleCategoriesList.subList(removePosition, removePosition + subCategories.size).clear()
        notifyItemRangeRemoved(removePosition, subCategories.size)
    }

    private fun expandOrCollapseCategoryItem(position: Int, category: CategoryItem) {
        if (category.isExpanded) {
            collapseCategory(position, category)
        } else {
            expandCategory(position, category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MAIN_CATEGORY_VIEW_TYPE -> MainCategoryViewHolder(
                ItemMainCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> SubCategoryViewHolder(
                ItemSubCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = visibleCategoriesList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = visibleCategoriesList[position]
        when (holder) {
            is MainCategoryViewHolder -> holder.bind(category, position)
            is SubCategoryViewHolder -> holder.bind(category)
        }
    }

    @SuppressLint("DiscouragedApi")
    inner class MainCategoryViewHolder(private val binding: ItemMainCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryItem, position: Int) {
            val context = binding.root.context
            binding.txtMainCategoryName.text = category.name

            // Set the main category image
            val resourceId = context.resources.getIdentifier(
                category.image.substringBeforeLast("."),
                "drawable",
                context.packageName
            )
            binding.ivMainCategory.setImageResource(
                if (resourceId != 0) resourceId else R.drawable.altin
            )
            binding.ivMainCategoryArrow.rotation = if (category.isExpanded) 180f else 0f

            binding.root.setOnClickListener {
                if (!category.subItems.isNullOrEmpty()) {
                    expandOrCollapseCategoryItem(position, category)
                    val newRotationAngle = if (category.isExpanded) 180f else 0f
                    ObjectAnimator.ofFloat(
                        binding.ivMainCategoryArrow,
                        "rotation",
                        binding.ivMainCategoryArrow.rotation,
                        newRotationAngle
                    ).apply {
                        duration = 300
                        interpolator = DecelerateInterpolator()
                        start()
                    }
                }
            }

            if (category.subItems.isNullOrEmpty()) {
                binding.ivMainCategoryArrow.setInvisible()
                binding.root.setOnClickListener {
                    onCategoryClick?.invoke(category.id)
                }
            } else {
                binding.ivMainCategoryArrow.setVisible()
            }

        }
    }

    inner class SubCategoryViewHolder(private val binding: ItemSubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryItem) {

            val parentCategory =
                visibleCategoriesList.find { it.subItems?.contains(category) == true }
            val subItems = parentCategory?.subItems

            binding.root.setOnClickListener {
                onCategoryClick?.invoke(category.id)
            }

            if (subItems != null) {
                when (category.id) {
                    subItems.first().id -> {
                        binding.root.setBackgroundResource(R.drawable.background_top_radius)
                    }
                    subItems.last().id -> {
                        binding.root.setBackgroundResource(R.drawable.background_bottom_radius)
                    }
                    else -> {
                        binding.root.setBackgroundResource(R.drawable.background_medium_items)
                    }
                }
            }
            binding.txtSubCategoryName.text = category.name
        }
    }

    companion object {
        private const val MAIN_CATEGORY_VIEW_TYPE = 1
        private const val SUB_CATEGORY_VIEW_TYPE = 0
    }
}
