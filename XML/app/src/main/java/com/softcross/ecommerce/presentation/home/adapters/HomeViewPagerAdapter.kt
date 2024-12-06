package com.softcross.ecommerce.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softcross.ecommerce.databinding.ItemViewpagerImageBinding

class HomeViewPagerAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder>() {

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
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(private val binding: ItemViewpagerImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(image: Int) {
                binding.imageView.setImageResource(image)
            }
    }
}
