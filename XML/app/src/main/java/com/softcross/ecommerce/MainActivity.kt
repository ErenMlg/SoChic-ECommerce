package com.softcross.ecommerce

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.apollographql.apollo.ApolloClient
import com.softcross.ecommerce.common.SoChicSingleton
import com.softcross.ecommerce.common.extensions.setInvisible
import com.softcross.ecommerce.common.extensions.setVisible
import com.softcross.ecommerce.data.network.GraphqlDataSourceImpl
import com.softcross.ecommerce.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNav = binding.bottomNav
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController, false)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.categoriesFragment -> setVisibleBottomNav()
                R.id.homeFragment -> setVisibleBottomNav()
                else -> setInvisibleBottomNav()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoChicSingleton.clearCategoryList()
    }

    fun setInvisibleBottomNav() {
        binding.bottomNav.setInvisible()
    }

    fun setVisibleBottomNav() {
        binding.bottomNav.setVisible()
    }
}