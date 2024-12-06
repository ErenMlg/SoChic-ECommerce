package com.softcross.ecommerce.presentation.products

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.extensions.toStrikeThroughText
import com.softcross.ecommerce.data.model.product.Product
import com.softcross.ecommerce.databinding.ItemProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var onProductClickListener:((String) -> Unit)? = null

    fun setOnProductClickListener(listener: (String) -> Unit) {
        onProductClickListener = listener
    }

    private val productList = mutableListOf<Product>()

    fun addProducts(newProducts: List<Product>) {
        val startPosition = productList.size
        productList.addAll(newProducts.distinct())
        notifyItemRangeInserted(startPosition, newProducts.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearProducts() {
        productList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(newProducts: List<Product>) {
        productList.clear()
        productList.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(productList[position])

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.root.setOnClickListener {
                Log.e("Adapter", product.toString())
                onProductClickListener?.invoke(product.id)
            }

            Glide.with(binding.ivItemPhoto.context)
                .load(product.images.firstOrNull())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.icon_loading)
                        .error(R.drawable.icon_error)
                        .centerCrop()
                )
                .into(binding.ivItemPhoto)

            binding.txtItemName.text = product.name
            binding.txtItemDiscountedPrice.text = product.discountedPrice

            if (product.discountedPrice == product.price) {
                binding.txtItemPrice.visibility = View.GONE
            } else {
                binding.txtItemPrice.visibility = View.VISIBLE
                binding.txtItemPrice.text = product.price.toStrikeThroughText()
            }
        }
    }
}
