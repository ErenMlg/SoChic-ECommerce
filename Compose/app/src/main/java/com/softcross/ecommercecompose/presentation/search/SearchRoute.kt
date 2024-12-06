package com.softcross.ecommercecompose.presentation.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.data.model.product.Product
import com.softcross.ecommercecompose.presentation.components.SearchTextField
import com.softcross.ecommercecompose.presentation.components.SoChicError
import com.softcross.ecommercecompose.presentation.components.SoChicLoading
import com.softcross.ecommercecompose.presentation.components.SoChicProduct
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.LightContainer
import com.softcross.ecommercecompose.presentation.theme.TextColor


@Composable
fun SearchRoute(
    uiState: SearchContract.SearchState,
    onAction: (SearchContract.SearchAction) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .imePadding()
            .fillMaxSize(),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchTextField(
                givenValue = uiState.searchQuery,
                placeHolder = "Arama",
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onAction(SearchContract.SearchAction.OnSearchDone)
                    }
                ),
                onValueChange = { onAction(SearchContract.SearchAction.OnSearchQueryChanged(it)) },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.icon_search),
                        contentDescription = "",
                        tint = HintColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(LightContainer)
                    .clickable {
                        keyboardController?.hide()
                        onAction(SearchContract.SearchAction.OnSearchDone)
                    }
            ) {
                Icon(
                    painterResource(R.drawable.icon_search),
                    contentDescription = "",
                    tint = HintColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (uiState.isLoading) {
            SoChicLoading()
        }
        if (uiState.isError) {
            SoChicError(
                errorMessage = uiState.errorMessage
            )
        }
        if (!uiState.isLoading && !uiState.isError) {
            SearchContent(
                productList = uiState.products,
                onReachEnd = { onAction(SearchContract.SearchAction.OnReachEndOfList); println("Sona gelindi") },
                isNewItemsLoading = uiState.isMoreLoading,
                onItemClick = { productId -> navigateToDetail(productId) }
            )
        }
    }
}

@Composable
fun SearchContent(
    productList: List<Product>,
    onReachEnd: () -> Unit,
    isNewItemsLoading: Boolean,
    onItemClick: (String) -> Unit
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = state,
        modifier = Modifier.padding(8.dp)
    ) {
        items(productList.size, { index -> productList[index].id }) {
            SoChicProduct(
                productList[it],
                onItemClick
            )
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchDark() {
    EcommerceComposeTheme {
        SearchRoute(
            uiState = SearchContract.SearchState(
                isLoading = false
            ),
            onAction = {},
            navigateToDetail = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SearchLight() {
    EcommerceComposeTheme {
        SearchRoute(
            uiState = SearchContract.SearchState(
                isLoading = false
            ),
            onAction = {},
            navigateToDetail = {}
        )
    }
}