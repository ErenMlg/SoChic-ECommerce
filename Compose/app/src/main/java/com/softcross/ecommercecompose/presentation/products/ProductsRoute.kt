package com.softcross.ecommercecompose.presentation.products

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.data.model.product.Product
import com.softcross.ecommercecompose.presentation.components.SoChicError
import com.softcross.ecommercecompose.presentation.components.SoChicLoading
import com.softcross.ecommercecompose.presentation.components.SoChicProduct
import com.softcross.ecommercecompose.presentation.components.SoChicText
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.PoppinsMedium
import com.softcross.ecommercecompose.presentation.theme.TextColor

@Composable
fun ProductsRoute(
    modifier: Modifier = Modifier,
    uiState: ProductsContract.UiState,
    onAction: (ProductsContract.UiAction) -> Unit,
    navigateToDetail: (String) -> Unit
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
            ProductsContent(
                categoryName = uiState.categoryName,
                totalItemCount = uiState.totalItems,
                productList = uiState.products,
                onReachEnd = { onAction(ProductsContract.UiAction.OnReachEndOfList) },
                isNewItemsLoading = uiState.isMoreLoading,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
fun ProductsContent(
    categoryName:String,
    totalItemCount:Int,
    productList:List<Product>,
    onReachEnd: () -> Unit,
    isNewItemsLoading: Boolean,
    navigateToDetail: (String) -> Unit
) {
    val state: LazyGridState = rememberLazyGridState()

    val isEndReached by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisibleItemIndex >= productList.lastIndex
        }
    }
    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            if (isEndReached) {
                onReachEnd()
            }
        }
    }

    SoChicText(
        text = categoryName,
        fontSize = 18.sp,
        fontFamily = PoppinsMedium,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    SoChicText(
        text = "($totalItemCount Sonuç Bulundu)",
        fontSize = 12.sp,
        color = HintColor,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = state,
        modifier = Modifier.padding(8.dp)
    ) {
        items(productList.size, { index -> productList[index].id }) {
            SoChicProduct(productList[it], navigateToDetail)
        }
        if (isNewItemsLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun ProductsLight() {
    EcommerceComposeTheme {
        ProductsRoute(
            uiState = ProductsContract.UiState(),
            onAction = {},
            navigateToDetail = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductsDark() {
    EcommerceComposeTheme {
        ProductsRoute(
            uiState = ProductsContract.UiState(),
            onAction = {},
            navigateToDetail = {}
        )
    }
}