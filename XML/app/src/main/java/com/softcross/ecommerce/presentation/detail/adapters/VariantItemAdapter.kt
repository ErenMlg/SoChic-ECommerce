package com.softcross.ecommerce.presentation.detail.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softcross.ecommerce.R
import com.softcross.ecommerce.data.model.product.VariantItem
import com.softcross.ecommerce.databinding.ItemProductVariantItemsBinding

class VariantItemAdapter : RecyclerView.Adapter<VariantItemAdapter.VariantItemViewHolder>() {

    /*
    Örneğin ürünün Renk değerinin altındaki değerleri gösteren bir adapter.
     */

    private val variantItems = mutableListOf<VariantItem>()
    private var onVariantClick : ((String) -> Unit)? = null
    private var activeItemID = ""

    @SuppressLint("NotifyDataSetChanged")
    fun setVariantItems(variantItems: List<VariantItem>) {
        this.variantItems.clear()
        this.variantItems.addAll(variantItems)
        notifyDataSetChanged()
    }

    fun setOnVariantClick(onVariantClick: (String) -> Unit) {
        this.onVariantClick = onVariantClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setActiveItemID(activeItemID: String) {
        this.activeItemID = activeItemID
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantItemViewHolder =
        VariantItemViewHolder(
            ItemProductVariantItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = variantItems.size

    override fun onBindViewHolder(holder: VariantItemViewHolder, position: Int) =
        holder.bind(variantItems[position])


    inner class VariantItemViewHolder(private val binding: ItemProductVariantItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(variantItem: VariantItem) = with(binding) {
            val context = root.context
            btnVariant.setOnClickListener {
                if (variantItem.inStock) {
                    onVariantClick?.invoke(variantItem.id)
                }
            }
            btnVariant.setStockStatus(variantItem.inStock)
            btnVariant.text = variantItem.name

            if (variantItem.inStock) {
                btnVariant.setTextColor(context.getColor(R.color.colorPrimary))
            } else {
                btnVariant.setTextColor(context.getColor(R.color.textColorHint))
            }

            if (variantItem.id == activeItemID) {
                btnVariant.setActive()
                btnVariant.setTextColor(context.getColor(R.color.colorGold))
            }
        }
    }

}