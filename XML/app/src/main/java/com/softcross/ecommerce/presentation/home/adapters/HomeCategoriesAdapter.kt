package com.softcross.ecommerce.presentation.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softcross.ecommerce.R
import com.softcross.ecommerce.databinding.ItemHomeCategoryBinding
import com.softcross.ecommerce.presentation.home.HomeItem

class HomeCategoriesAdapter : RecyclerView.Adapter<HomeCategoriesAdapter.CategoriesViewHolder>() {

    private var onCategoryItemClickListener: ((String) -> Unit)? = null

    private val categoriesList = mutableListOf<HomeItem>()

    fun setOnCategoryItemClickListener(listener: (String) -> Unit) {
        onCategoryItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategoryList(categoryList: List<HomeItem>) {
        with(this.categoriesList) {
            clear()
            addAll(categoryList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder =
        CategoriesViewHolder(
            ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = categoriesList.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) = holder.bind(categoriesList[position])

    @SuppressLint("DiscouragedApi")
    inner class CategoriesViewHolder(private val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(category: HomeItem) {
                binding.root.setOnClickListener {
                    onCategoryItemClickListener?.invoke(category.id.toString())
                }
                val context = binding.root.context
                val resourceId = context.resources.getIdentifier(
                    category.imageUrl.substringBeforeLast("."),
                    "drawable",
                    context.packageName
                )
                binding.txtCategoryName.text = category.name.substringAfter("So CHIC ")
                if (resourceId != 0) {
                    binding.ivCategoryImage.setImageResource(resourceId)
                } else {
                    binding.ivCategoryImage.setImageResource(R.drawable.altin)
                }
            }
    }

}