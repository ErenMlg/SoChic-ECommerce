package com.softcross.ecommercecompose.navigation

import com.softcross.ecommercecompose.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
) {
    object Home : BottomNavItem("home", R.drawable.icon_home, "Anasayfa")
    object Categories : BottomNavItem("categories", R.drawable.icon_categories, "Kategoriler")
    object Search : BottomNavItem("search", R.drawable.icon_search, "Arama")
    object Cart : BottomNavItem("search", R.drawable.icon_cart, "Sepet")
    object Account : BottomNavItem("search", R.drawable.icon_avatar, "Hesabım")
}
