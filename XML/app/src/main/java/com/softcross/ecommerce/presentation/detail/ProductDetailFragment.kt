package com.softcross.ecommerce.presentation.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.softcross.ecommerce.R.drawable
import com.softcross.ecommerce.R.layout
import com.softcross.ecommerce.R.raw
import com.softcross.ecommerce.common.delegate.viewBinding
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.common.extensions.toStrikeThroughText
import com.softcross.ecommerce.data.model.product.ProductDetail
import com.softcross.ecommerce.databinding.DialogZoomImageBinding
import com.softcross.ecommerce.databinding.FragmentProductDetailBinding
import com.softcross.ecommerce.presentation.detail.adapters.ProductVariantAdapter
import com.softcross.ecommerce.presentation.detail.adapters.ProductViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment(layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()
    private val variantAdapter = ProductVariantAdapter().apply {
        setOnVariantClick { viewModel.getProductDetail(it) }
    }
    private val viewPagerAdapter = ProductViewPagerAdapter().apply {
        setOnImageClick { showZoomDialog(it); println("VP TIKLANDI $it") }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductDetail(args.productID)
        setupPage()
        observeUI()
    }

    private fun setupPage() {
        val viewPager = binding.vpImages
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 0
        binding.rvProductVariants.adapter = variantAdapter
    }

    private fun observeUI() {
        viewModel.productUiState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> handleLoading()

                state.isError -> handleError(state.errorMessage)

                else -> {
                    if (state.product != null) {
                        handleSuccess(state.product)
                    }
                }
            }
        }
    }

    // Ürüne tıklandığında büyütme ekranını açıyor.
    private fun showZoomDialog(imageUrl: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogZoomImageBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.root,
            false
        )
        // Load the image (you can use Glide or BitmapFactory here)
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(drawable.icon_loading)
            .error(drawable.icon_error)
            .into(dialogBinding.zoomableImageView)

        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }


    private fun handleError(errorMessage: String) {
        setInvisiblePage()
        binding.apply {
            lottieText.text = errorMessage
            lottieView.setAnimation(raw.error)
            lottieView.speed = 1f
        }
    }

    private fun handleLoading() {
        setInvisiblePage()
        binding.apply {
            lottieText.text = "Loading..."
            lottieView.setAnimation(raw.loading)
            lottieView.speed = 3f
        }
    }

    private fun handleSuccess(productDetail: ProductDetail) {
        setVisiblePage()
        variantAdapter.setVariants(productDetail.variants)
        variantAdapter.setActiveItemID(productDetail.id)
        viewPagerAdapter.setImages(productDetail.images)
        setupViewPagerIndicator(productDetail.images)
        if (productDetail.discountedPrice == productDetail.price) {
            binding.txtProductDiscountedPrice.text = productDetail.discountedPrice
            binding.txtProductPrice.setInvisible()
        } else {
            binding.txtProductPrice.text = productDetail.price.toStrikeThroughText()
            binding.txtProductDiscountedPrice.text = productDetail.discountedPrice
        }
        binding.txtProductName.text = productDetail.name
        binding.txtProductDetail.text = productDetail.fullDescription.trim()
    }

    // Ürün resimlerinin altında bulunan noktaları oluşturur.
    private fun setupViewPagerIndicator(images: List<String>) {
        val indicatorLayout = binding.indicatorLayout
        indicatorLayout.removeAllViews()
        val viewPager = binding.vpImages

        for (i in images.indices) {
            val dot = ImageView(requireContext())
            dot.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8
            }
            dot.setImageResource(if (i == 0) drawable.indicator_dot_active else drawable.indicator_dot)
            indicatorLayout.addView(dot)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until indicatorLayout.childCount) {
                    val dot = indicatorLayout.getChildAt(i) as ImageView
                    dot.setImageResource(if (i == position) drawable.indicator_dot_active else drawable.indicator_dot)
                }
            }
        })
    }

    private fun setInvisiblePage() {
        with(binding) {
            frameLayout4.setInvisible()
            loadingLayout.setVisible()
        }
    }

    private fun setVisiblePage() {
        with(binding) {
            frameLayout4.setVisible()
            loadingLayout.setInvisible()
        }
    }
}