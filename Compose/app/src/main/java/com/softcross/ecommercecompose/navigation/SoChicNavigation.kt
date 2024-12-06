package com.softcross.ecommercecompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

@Composable
fun SoChicNavigation(navHostController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navHostController,
        startDestination = Home.route,
    ) {
        composable(Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeRoute(
                uiState = uiState,
                modifier = modifier,
                navigateToCategories = { navHostController.navigate(Categories.route) },
                navigateToProducts = { categoryId ->
                    navHostController.navigate("${Products.route}/$categoryId")
                }
            )
        }

        composable(Categories.route) {
            val viewModel: CategoriesViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CategoriesRoute(
                uiState = uiState,
                modifier = modifier,
                navigateToProducts = { categoryId ->
                    navHostController.navigate("${Products.route}/$categoryId")
                }
            )
        }

        composable(Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            SearchRoute(
                uiState = uiState,
                onAction = viewModel::onAction,
                modifier = modifier,
                navigateToDetail = { productId ->
                    navHostController.navigate("${Detail.route}/$productId")
                }
            )
        }

        composable(Products.routeWithArgs, Products.arguments) {
            val viewModel: ProductsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ProductsRoute(
                uiState = uiState,
                onAction = viewModel::onAction,
                modifier = modifier,
                navigateToDetail = { productId ->
                    navHostController.navigate("${Detail.route}/$productId")
                }
            )
        }

        composable(Detail.routeWithArgs, Detail.arguments) {
            val viewModel: DetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            DetailRoute(
                uiState = uiState,
                modifier = modifier,
                onAction = viewModel::onAction
            )
        }
    }
}