package com.softcross.ecommercecompose.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route:String
}

object Home: Destination {
    override val route: String = "home"
}

object Categories: Destination {
    override val route: String = "categories"
}

object Products: Destination {
    override val route: String = "products"
    val routeWithArgs = "products/{categoryID}"
    val arguments = listOf(
        navArgument("categoryID"){
            type = NavType.StringType
        }
    )
}

object Detail: Destination {
    override val route: String = "detail"
    val routeWithArgs = "detail/{productID}"
    val arguments = listOf(
        navArgument("productID"){
            type = NavType.StringType
        }
    )
}

object Search: Destination {
    override val route: String = "search"
}