package com.softcross.ecommerce.presentation.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.delegate.viewBinding
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.data.model.product.Product
import com.softcross.ecommerce.databinding.FragmentProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private val viewModel by viewModels<ProductViewModel>()
    private val binding: FragmentProductBinding by viewBinding(FragmentProductBinding::bind)
    private val args: ProductFragmentArgs by navArgs()
    private val adapter = ProductAdapter().apply {
        setOnProductClickListener { product ->
            findNavController().navigate(
                ProductFragmentDirections.productToDetail(product)
            )
        }
    }
    private var currentPage = 1
    private var isMoreLoaded = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rwProducts.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rwProducts.adapter = adapter

        // Ürünlerin sonuna gelindiğinde daha fazla ürün yüklenir. Eğer son sayfa ise daha fazla ürün yüklenmez.
        binding.rwProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null)
                val pastVisibleItems = firstVisibleItems.minOrNull() ?: 0

                if (isMoreLoaded && dy > 0 && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    isMoreLoaded = false
                    if (currentPage < (viewModel.productUiState.value?.totalPages ?: 0)) {
                        loadMoreProducts()
                        binding.pbProduct.setVisible()
                    }
                }
            }
        })
    }

    private fun observeUI() {
        viewModel.productUiState.observe(viewLifecycleOwner) {
            when {
                it.isError -> handleError(it.errorMessage)

                it.isLoading -> handleLoading()

                else -> {
                    isMoreLoaded = true
                    binding.pbProduct.setInvisible()
                    setVisibleProducts()
                    binding.txtCategoryName.text = it.categoryName
                    binding.txtItemCount.text =
                        requireContext().getString(R.string.total_items_text, it.totalItems)
                    handleSuccess(it.products)
                }
            }
        }
    }

    private fun handleSuccess(productList: List<Product>) {
        if (currentPage == 1){
            adapter.updateProductList(productList)
        } else {
            adapter.addProducts(productList)
        }
    }

    // En sona gelindiğinde daha fazla ürün yüklenir. Bunu yaparken current page arttırılır ve yeni ürünler getirilir.
    private fun loadMoreProducts() {
        currentPage++
        viewModel.getProducts(args.categoryID, currentPage)
    }

    private fun handleError(errorMessage: String) {
        setInvisibleProducts()
        binding.apply {
            lottieView.setAnimation(R.raw.error)
            lottieText.text = errorMessage
            lottieView.speed = 1f
        }
    }

    private fun handleLoading() {
        setInvisibleProducts()
        binding.apply {
            lottieView.setAnimation(R.raw.loading)
            lottieText.text = "Loading..."
            lottieView.speed = 3f
        }
    }

    private fun setInvisibleProducts() {
        with(binding) {
            txtItemCount.setInvisible()
            txtCategoryName.setInvisible()
            rwProducts.setInvisible()
            lottieView.setVisible()
            lottieText.setVisible()
        }
    }

    private fun setVisibleProducts() {
        with(binding) {
            txtItemCount.setVisible()
            txtCategoryName.setVisible()
            rwProducts.setVisible()
            lottieView.setInvisible()
            lottieText.setInvisible()
        }
    }

}
