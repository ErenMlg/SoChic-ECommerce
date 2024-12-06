package com.softcross.ecommerce.presentation.detail.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.softcross.ecommerce.R
import com.softcross.ecommerce.databinding.ItemViewpagerImageBinding

class ProductViewPagerAdapter :
    RecyclerView.Adapter<ProductViewPagerAdapter.ViewHolder>() {

    /*
    ViewPagerin kullandığı adapter sınıfıdır. Bu sınıf, ürün detay sayfasında ürün resimlerini göstermek için kullanılır.
     */

    private var imagesList = mutableListOf<String>()
    private var onImageClick: ((String) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(images: List<String>) {
        imagesList.clear()
        imagesList.addAll(images)
        notifyDataSetChanged()
    }

    fun setOnImageClick(onImageClick: (String) -> Unit) {
        this.onImageClick = onImageClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewpagerImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    inner class ViewHolder(private val binding: ItemViewpagerImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            binding.root.setOnClickListener {
                onImageClick?.invoke(image)
            }
            Glide.with(binding.root.context)
            .load(image)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.icon_loading)
                        .error(R.drawable.icon_error)
                        .centerCrop()
                )
                .into(binding.imageView)
        }
    }
}
