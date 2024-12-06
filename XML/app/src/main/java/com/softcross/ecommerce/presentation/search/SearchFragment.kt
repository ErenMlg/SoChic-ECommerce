package com.softcross.ecommerce.presentation.search

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.delegate.viewBinding
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.data.model.product.Product
import com.softcross.ecommerce.databinding.FragmentSearchBinding
import com.softcross.ecommerce.presentation.products.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val adapter = ProductAdapter().apply {
        setOnProductClickListener { product ->
            findNavController().navigate(
                SearchFragmentDirections.searchToDetail(product)
            )
        }
    }
    private var currentPage = 1
    private var isMoreLoaded = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPage()
        observeUI()
        setInvisibleErrorLoading()
    }

    private fun setupPage() {
        binding.rvSearchedProducts.adapter = adapter
        binding.rvSearchedProducts.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchProduct()
                true
            } else {
                false
            }
        }
        binding.button2.setOnClickListener { searchProduct() }
        // Ürünlerin sonuna gelindiğinde daha fazla ürün yüklenir. Eğer son sayfa ise daha fazla ürün yüklenmez.
        binding.rvSearchedProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null)
                val pastVisibleItems = firstVisibleItems.minOrNull() ?: 0

                if (isMoreLoaded && dy > 0 && (visibleItemCount + pastVisibleItems) >= totalItemCount && viewModel.searchUiState.value?.products?.isNotEmpty() == true) {
                    isMoreLoaded = false
                    if (currentPage < (viewModel.searchUiState.value?.totalPages ?: 0)) {
                        loadMoreProducts()
                        binding.pbProduct.setVisible()
                    }
                }
            }
        })
    }

    // En sona gelindiğinde daha fazla ürün yüklenir. Bunu yaparken current page arttırılır ve yeni ürünler getirilir.
    private fun loadMoreProducts() {
        val query = binding.searchText.text.toString()
        currentPage++
        if (query.isNotEmpty()) {
            viewModel.searchProduct(query, currentPage)
        }
    }

    private fun searchProduct() {
        adapter.clearProducts()
        val query = binding.searchText.text.toString()
        if (query.isNotEmpty()) {
            viewModel.searchProduct(query, 1)
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchText.windowToken, 0)
    }

    private fun observeUI() {
        viewModel.searchUiState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> handleLoading()
                state.isError -> handleError(state.errorMessage)
                else -> {
                    isMoreLoaded = true
                    binding.pbProduct.setInvisible()
                    handleSuccess(state.products)
                }
            }
        }
    }

    private fun handleSuccess(items: List<Product>) {
        setInvisibleErrorLoading()
        adapter.addProducts(items)
    }

    private fun handleError(errorMessage: String) {
        setVisibleErrorLoading()
        binding.apply {
            lottieView.setAnimation(R.raw.error)
            lottieText.text = errorMessage
            lottieView.speed = 1f
        }
    }

    private fun handleLoading() {
        setVisibleErrorLoading()
        binding.apply {
            lottieView.setAnimation(R.raw.loading)
            lottieText.text = "Loading..."
            lottieView.speed = 3f
        }
    }

    private fun setInvisibleErrorLoading() {
        with(binding) {
            lottieView.setInvisible()
            lottieText.setInvisible()
        }
    }

    private fun setVisibleErrorLoading() {
        if (adapter.itemCount == 0) {
            with(binding) {
                lottieView.setVisible()
                lottieText.setVisible()
            }
        }
    }

}