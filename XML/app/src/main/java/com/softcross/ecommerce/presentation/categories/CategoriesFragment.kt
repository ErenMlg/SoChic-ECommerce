package com.softcross.ecommerce.presentation.categories

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.delegate.viewBinding
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val viewModel: CategoriesViewModel by viewModels()
    private val binding: FragmentCategoriesBinding by viewBinding(FragmentCategoriesBinding::bind)
    private val adapter = CategoriesAdapter().apply {
        setOnCategoryClick {
            findNavController().navigate(
                CategoriesFragmentDirections.categoryToProducts(it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()
        setupRv()
    }

    // Sayfa oluşurken recyclerview ayarlanır.
    private fun setupRv() {
        binding.rvCategories.adapter = adapter
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
    }

    // UI durumlarına göre sayfa güncellenir.
    private fun observeUI() {
        viewModel.categoriesUiState.observe(viewLifecycleOwner) {
            when {
                it.isLoading -> handleLoading()

                it.isError -> handleError(it.errorMessage)

                else -> handleSuccess(it.categories)
            }
        }
    }

    private fun handleLoading() {
        setInvisiblePage()
        binding.apply {
            lottieView.setAnimation(R.raw.loading)
            lottieText.text = "Loading..."
            lottieView.speed = 3f
        }
    }

    private fun handleError(errorMessage: String) {
        binding.apply {
            lottieView.setAnimation(R.raw.error)
            lottieText.text = errorMessage
            lottieView.speed = 1f
        }
    }

    private fun handleSuccess(categories: List<CategoryItem>) {
        setVisiblePage()
        adapter.setCategoryList(categories)
    }

    // Sayfa yüklenirken görünürlük ayarları yapılır.
    private fun setInvisiblePage() {
        with(binding) {
            textView.setInvisible()
            txtLogoCategories.setInvisible()
            rvCategories.setInvisible()
            lottieView.setVisible()
            lottieText.setVisible()
        }
    }

    // Sayfa yüklendikten sonra görünürlük ayarları yapılır.
    private fun setVisiblePage() {
        with(binding) {
            textView.setVisible()
            txtLogoCategories.setVisible()
            rvCategories.setVisible()
            lottieView.setInvisible()
            lottieText.setInvisible()
        }
    }
}