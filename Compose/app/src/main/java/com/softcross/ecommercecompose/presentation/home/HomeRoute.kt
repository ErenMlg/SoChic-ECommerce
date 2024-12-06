package com.softcross.ecommercecompose.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.presentation.components.PagerIndicator
import com.softcross.ecommercecompose.presentation.components.SoChicError
import com.softcross.ecommercecompose.presentation.components.SoChicLoading
import com.softcross.ecommercecompose.presentation.components.SoChicText
import com.softcross.ecommercecompose.presentation.theme.Container
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.PoppinsLight
import com.softcross.ecommercecompose.presentation.theme.PoppinsMedium
import com.softcross.ecommercecompose.presentation.theme.TextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

val images = listOf(
    R.drawable.banner,
    R.drawable.banner1,
    R.drawable.banner2,
    R.drawable.banner3,
    R.drawable.banner4,
    R.drawable.banner5
)

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    uiState: HomeContract.UiState,
    navigateToProducts: (String) -> Unit,
    navigateToCategories: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(TextColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .scale(scaleX = 0.8f, scaleY = 1f)
        )
        if (uiState.isLoading) {
            SoChicLoading()
        }
        if (uiState.isError) {
            SoChicError(
                errorMessage = uiState.errorMessage
            )
        }
        if (!uiState.isLoading && !uiState.isError) {
            HomeScreen(
                categories = uiState.categoryList,
                navigateToProducts = navigateToProducts,
                navigateToCategories = navigateToCategories
            )
        }
    }
}

@Composable
fun HomeScreen(
    categories: List<HomeItem>,
    navigateToProducts: (String) -> Unit,
    navigateToCategories: () -> Unit
) {
    val context = LocalContext.current
    val conf = LocalConfiguration.current
    val pagerState = rememberPagerState { images.size }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Auto-scroll effect
    LaunchedEffect(isPressed) {
        while (true) {
            if (!isPressed) {
                delay(5000)
                val nextPage = pagerState.currentPage + 1
                if (nextPage > images.size - 1) {
                    pagerState.scrollToPage(0)
                } else {
                    pagerState.animateScrollToPage(
                        nextPage,
                        animationSpec = tween(2000)
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        PagerIndicator(
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            size = images.size
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_shop),
            contentDescription = "Shop",
            tint = TextColor,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 4.dp)
        )
        SoChicText(
            text = "So CHIC'e hoş geldiniz!",
            fontFamily = PoppinsMedium,
            fontSize = 24.sp,
        )
    }
    SoChicText(
        text = "En güzel ürünleri en uygun fiyatlarla alabilir ve kendi zevklerinize göre tasarım yapabilirsiniz...",
        fontFamily = PoppinsLight,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp, start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SoChicText(
            text = "Ana Kategoriler",
            fontFamily = PoppinsMedium,
        )
        SoChicText(
            text = "Tümünü gör",
            fontSize = 12.sp,
            color = HintColor,
            fontFamily = PoppinsMedium,
            modifier = Modifier.clickable { navigateToCategories() }
        )
    }
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 8.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(count = categories.size, key = { categories[it].id }) { index ->
            val item = categories[index]
            val image = context.resources.getIdentifier(
                item.imageUrl.substringBeforeLast("."),
                "drawable",
                context.packageName
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Container)
                    .clickable { navigateToProducts(item.id.toString()) }
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .height((conf.screenHeightDp * 0.2f).dp)
                        .scale(1.1f)
                        .clip(RoundedCornerShape(8.dp))
                )
                SoChicText(
                    text = item.name,
                    fontFamily = PoppinsLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeDark() {
    EcommerceComposeTheme {
        HomeRoute(
            uiState = HomeContract.UiState(),
            navigateToCategories = {},
            navigateToProducts = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun HomeLight() {
    EcommerceComposeTheme {
        HomeRoute(
            uiState = HomeContract.UiState(),
            navigateToCategories = {},
            navigateToProducts = {}
        )
    }
}