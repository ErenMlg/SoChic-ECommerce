package com.softcross.ecommercecompose.presentation.detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.data.model.product.ProductDetail
import com.softcross.ecommercecompose.presentation.components.PagerIndicator
import com.softcross.ecommercecompose.presentation.components.SoChicCoilImage
import com.softcross.ecommercecompose.presentation.components.SoChicError
import com.softcross.ecommercecompose.presentation.components.SoChicLoading
import com.softcross.ecommercecompose.presentation.components.SoChicText
import com.softcross.ecommercecompose.presentation.theme.DarkContainer
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import com.softcross.ecommercecompose.presentation.theme.GoldColor
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.LightBackground
import com.softcross.ecommercecompose.presentation.theme.LightContainer
import com.softcross.ecommercecompose.presentation.theme.PoppinsBold
import com.softcross.ecommercecompose.presentation.theme.PoppinsLight
import com.softcross.ecommercecompose.presentation.theme.PoppinsMedium
import com.softcross.ecommercecompose.presentation.theme.TextColor

@Composable
fun DetailRoute(
    modifier: Modifier = Modifier,
    uiState: DetailContract.UiState,
    onAction: (DetailContract.UiAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (uiState.isLoading) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                colorFilter = ColorFilter.tint(TextColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .scale(scaleX = 0.8f, scaleY = 1f)
            )
            SoChicLoading()
        }
        if (uiState.isError) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                colorFilter = ColorFilter.tint(TextColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .scale(scaleX = 0.8f, scaleY = 1f)
            )
            SoChicError(
                errorMessage = uiState.errorMessage
            )
        }
        if (!uiState.isLoading && !uiState.isError && uiState.product != null) {
            DetailContent(
                product = uiState.product,
                onVariantClick = { variantId ->
                    onAction(DetailContract.UiAction.OnVariantSelected(variantId))
                }
            )
        }
    }
}

@Composable
fun DetailContent(
    product: ProductDetail,
    onVariantClick: (String) -> Unit
) {
    val conf = LocalConfiguration.current
    val pagerState = rememberPagerState { product.images.size }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(TextColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .scale(scaleX = 0.8f, scaleY = 1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier,
            ) { page ->
                SoChicCoilImage(
                    model = product.images[page],
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((conf.screenHeightDp * 0.6f).dp)
                )
            }
            PagerIndicator(
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                size = product.images.size
            )
        }
        SoChicText(
            text = product.name,
            fontFamily = PoppinsBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.size(8.dp))
        SoChicText(
            text = product.fullDescription,
            fontSize = 12.sp,
            fontFamily = PoppinsLight,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            items(product.variants.size) {
                val currentVariant = product.variants[it]
                Column {
                    SoChicText(
                        text = currentVariant.name,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.Start)
                    )
                    LazyRow {
                        items(currentVariant.items.size, { currentVariant.items[it].id }) {
                            val currentVariantItem = currentVariant.items[it]
                            val textColor = if (currentVariantItem.id == product.id) {
                                GoldColor
                            } else {
                                if (currentVariantItem.inStock) TextColor else HintColor
                            }
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (currentVariantItem.id == product.id) {
                                            Modifier.border(
                                                1.dp,
                                                GoldColor,
                                                RoundedCornerShape(8.dp)
                                            )
                                        } else {
                                            if (currentVariantItem.inStock) {
                                                Modifier.border(
                                                    1.dp,
                                                    TextColor,
                                                    RoundedCornerShape(8.dp)
                                                )
                                            } else {
                                                Modifier
                                                    .border(
                                                        1.dp,
                                                        HintColor,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .drawBehind {
                                                        drawLine(
                                                            color = HintColor,
                                                            start = Offset(0f, 0f),
                                                            end = Offset(size.width, size.height),
                                                            strokeWidth = 1.dp.toPx()
                                                        )
                                                        drawLine(
                                                            color = HintColor,
                                                            start = Offset(size.width, 0f),
                                                            end = Offset(0f, size.height),
                                                            strokeWidth = 1.dp.toPx()
                                                        )
                                                    }
                                            }
                                        }
                                    )
                                    .clickable {
                                        if (currentVariantItem.inStock){
                                            onVariantClick(currentVariantItem.id)
                                        }
                                    }
                            ) {
                                SoChicText(
                                    text = currentVariantItem.name,
                                    fontSize = 14.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            SoChicText(
                text = product.discountedPrice,
                fontSize = 18.sp,
                fontFamily = PoppinsBold
            )
            if (product.discountedPrice != product.price) {
                SoChicText(
                    text = product.price,
                    fontSize = 12.sp,
                    color = HintColor,
                    fontFamily = PoppinsLight,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkContainer,
                disabledContainerColor = LightContainer
            ),
            modifier = Modifier.padding(end = 16.dp)
        ) {
            SoChicText(
                text = "Sepete Ekle",
                fontSize = 14.sp,
                color = LightBackground,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DetailLight() {
    EcommerceComposeTheme {
        DetailRoute(
            uiState = DetailContract.UiState(),
            onAction = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailDark() {
    EcommerceComposeTheme {
        DetailRoute(
            uiState = DetailContract.UiState(),
            onAction = {}
        )
    }
}