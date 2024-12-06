package com.softcross.ecommercecompose.presentation

import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.ecommercecompose.navigation.Categories
import com.softcross.ecommercecompose.navigation.Home
import com.softcross.ecommercecompose.navigation.Search
import com.softcross.ecommercecompose.navigation.SoChicBottomNavigationBar
import com.softcross.ecommercecompose.navigation.SoChicNavigation
import com.softcross.ecommercecompose.presentation.categories.CategoriesRoute
import com.softcross.ecommercecompose.presentation.categories.CategoriesViewModel
import com.softcross.ecommercecompose.presentation.detail.DetailRoute
import com.softcross.ecommercecompose.presentation.detail.DetailViewModel
import com.softcross.ecommercecompose.presentation.home.HomeRoute
import com.softcross.ecommercecompose.presentation.home.HomeViewModel
import com.softcross.ecommercecompose.presentation.products.ProductsRoute
import com.softcross.ecommercecompose.presentation.products.ProductsViewModel
import com.softcross.ecommercecompose.presentation.search.SearchRoute
import com.softcross.ecommercecompose.presentation.search.SearchViewModel
import com.softcross.ecommercecompose.presentation.theme.Background
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceComposeTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val bottomBarState = rememberSaveable { (mutableStateOf(false)) }

                when (navBackStackEntry?.destination?.route) {
                    Home.route -> bottomBarState.value = true
                    Search.route -> bottomBarState.value = true
                    Categories.route -> bottomBarState.value = true
                    else -> bottomBarState.value = false
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        SoChicBottomNavigationBar(
                            navController = navController,
                            bottomBarState = bottomBarState
                        )
                    },
                    containerColor = Background,
                ) { innerPadding ->
                    SoChicNavigation(
                        navController,
                        Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceComposeTheme {
    }
}