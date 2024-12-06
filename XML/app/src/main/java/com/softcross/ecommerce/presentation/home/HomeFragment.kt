package com.softcross.ecommerce.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.softcross.ecommerce.MainActivity
import com.softcross.ecommerce.R
import com.softcross.ecommerce.common.delegate.viewBinding
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.databinding.FragmentHomeBinding
import com.softcross.ecommerce.presentation.home.adapters.HomeCategoriesAdapter
import com.softcross.ecommerce.presentation.home.adapters.HomeViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private val adapter = HomeCategoriesAdapter().apply {
        setOnCategoryItemClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToFragment(it))
        }
    }
    private val images = listOf(
        R.drawable.banner,
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.banner4,
        R.drawable.banner5
    )
    // Auto scroll yapmak için gerekli olan handler ve runnable
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            val nextItem =
                (binding.viewPager.currentItem + 1) % (binding.viewPager.adapter?.itemCount ?: 1)
            binding.viewPager.currentItem = nextItem
            handler.postDelayed(this, 4000L)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()
        setupRv()
        setupViewPager()
    }

    private fun setupRv() {
        binding.txtMoreCategory.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()) }
        binding.rwCategory.adapter = adapter
        binding.rwCategory.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    // ViewPager2 ve indicator'ları set ediyoruz.
    private fun setupViewPager() {
        val viewPager = binding.viewPager
        viewPager.adapter = HomeViewPagerAdapter(images)
        viewPager.currentItem = 0

        val indicatorLayout = binding.indicatorLayout
        for (i in images.indices) {
            val dot = ImageView(requireContext())
            dot.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8
            }
            dot.setImageResource(if (i == 0) R.drawable.indicator_dot_active else R.drawable.indicator_dot)
            indicatorLayout.addView(dot)
        }
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until indicatorLayout.childCount) {
                    val dot = indicatorLayout.getChildAt(i) as ImageView
                    dot.setImageResource(if (i == position) R.drawable.indicator_dot_active else R.drawable.indicator_dot)
                }
            }
        }
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    private fun observeUI() {
        viewModel.homeScreenUiState.observe(viewLifecycleOwner) {
            when {
                it.isError -> handleError(it.errorMessage)

                it.isLoading -> handleLoading()

                else -> handleHomeScreen(it.categories)
            }
        }
    }

    private fun handleError(errorMessage: String) {
        setInvisibleHome()
        binding.apply {
            lottieText.text = errorMessage
            lottieView.setAnimation(R.raw.error)
            lottieView.speed = 1f
        }
    }

    private fun handleLoading() {
        stopAutoScroll()
        (activity as MainActivity).setInvisibleBottomNav()
        setInvisibleHome()
        binding.apply {
            lottieText.text = "Loading..."
            lottieView.setAnimation(R.raw.loading)
            lottieView.speed = 3f
        }
    }

    private fun handleHomeScreen(categories: List<HomeItem>) {
        startAutoScroll()
        (activity as MainActivity).setVisibleBottomNav()
        setVisibleHome()
        adapter.updateCategoryList(categories)
    }

    private fun startAutoScroll() {
        handler.postDelayed(runnable, 4000L)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(runnable)
    }

    private fun setInvisibleHome() {
        with(binding) {
            lottieText.setVisible()
            lottieView.setVisible()
            rwCategory.setInvisible()
            txtLogo.setInvisible()
            ivBanner.setInvisible()
            txtCategory.setInvisible()
            txtMoreCategory.setInvisible()
            txtWelcome.setInvisible()
            txtWelcomeSubtitle.setInvisible()
            imageView.setInvisible()
        }
    }

    private fun setVisibleHome() {
        with(binding) {
            lottieText.setInvisible()
            lottieView.setInvisible()
            rwCategory.setVisible()
            txtLogo.setVisible()
            ivBanner.setVisible()
            txtCategory.setVisible()
            txtMoreCategory.setVisible()
            txtWelcome.setVisible()
            txtWelcomeSubtitle.setVisible()
            imageView.setVisible()
        }
    }

    // Fragment destroy olduğunda handler ve runnable'ı kaldırıyoruz bu sayede MemoryLeak sorunun önüne geçmiş oluyoruz.
    // LeakCanary ile kontrol edildi.
    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        handler.removeCallbacks(runnable)
        stopAutoScroll()
        binding.rwCategory.adapter = null
    }

}