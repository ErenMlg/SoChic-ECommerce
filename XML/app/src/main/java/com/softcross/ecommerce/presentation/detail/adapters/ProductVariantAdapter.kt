package com.softcross.ecommerce.presentation.detail.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softcross.ecommerce.data.model.product.Variant
import com.softcross.ecommerce.databinding.ItemProductVariantsBinding

class ProductVariantAdapter : RecyclerView.Adapter<ProductVariantAdapter.VariantViewHolder>() {

    /*
    Ürünün Renk, Madde gibi özelliklerinin başlıkları ve altında bu özelliklere ait seçeneklerin listelendiği adapter sınıfıdır.
     */

    private val variantList = mutableListOf<Variant>()
    private val adapter = VariantItemAdapter()

    @SuppressLint("NotifyDataSetChanged")
    fun setVariants(variants: List<Variant>) {
        variantList.clear()
        variantList.addAll(variants)
        notifyDataSetChanged()
    }

    fun setActiveItemID(id: String) {
        adapter.setActiveItemID(id)
    }

    fun setOnVariantClick(onVariantClick: (String) -> Unit) {
        adapter.setOnVariantClick(onVariantClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder =
        VariantViewHolder(
            ItemProductVariantsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = variantList.size

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) =
        holder.bind(variantList[position])

    inner class VariantViewHolder(private val binding: ItemProductVariantsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(variant: Variant) {
            binding.txtVariantHeader.text = variant.name
            binding.rvVariantItems.adapter = adapter
            adapter.setVariantItems(variant.items)
        }
    }

}