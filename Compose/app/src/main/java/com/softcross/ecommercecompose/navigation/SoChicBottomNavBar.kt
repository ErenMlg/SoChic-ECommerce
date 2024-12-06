package com.softcross.ecommercecompose.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.ecommercecompose.presentation.components.SoChicText
import com.softcross.ecommercecompose.presentation.theme.Container
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.TextColor

@Composable
fun SoChicBottomNavigationBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Account
    )
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomNavigation(
            modifier = Modifier.padding(
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            ).padding(vertical = 8.dp, horizontal = 16.dp).clip(CircleShape),
            backgroundColor = Container,
            elevation = 8.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier.width(20.dp),
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.label,
                            tint = HintColor,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        SoChicText(
                            text = item.label,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = remember {
                        {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    selectedContentColor = TextColor,
                    unselectedContentColor = Color.DarkGray,
                    alwaysShowLabel = true
                )
            }
        }
    }
}
