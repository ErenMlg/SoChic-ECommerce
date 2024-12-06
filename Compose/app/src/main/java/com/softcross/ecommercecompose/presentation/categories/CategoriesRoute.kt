package com.softcross.ecommercecompose.presentation.categories

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.presentation.components.SoChicError
import com.softcross.ecommercecompose.presentation.components.SoChicLoading
import com.softcross.ecommercecompose.presentation.components.SoChicText
import com.softcross.ecommercecompose.presentation.theme.Container
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme
import com.softcross.ecommercecompose.presentation.theme.PoppinsMedium
import com.softcross.ecommercecompose.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CategoriesRoute(
    modifier: Modifier = Modifier,
    uiState: CategoriesContract.UiState,
    navigateToProducts: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
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
            CategoriesContent(
                categoryList = uiState.categoryList,
                onItemClick = navigateToProducts
            )
        }
    }
}

@Composable
fun CategoriesContent(
    categoryList: List<CategoryItem>,
    onItemClick: (String) -> Unit
) {
    val context = LocalContext.current
    val conf = LocalConfiguration.current
    val categories = remember { categoryList.toMutableStateList() }
    SoChicText(
        text = "Kategoriler",
        fontFamily = PoppinsMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        items(categories.size, key = { index -> categories[index].id }) { index ->
            Column {
                val currentCategory = categories[index]
                val image = context.resources.getIdentifier(
                    currentCategory.image.substringBeforeLast("."),
                    "drawable",
                    context.packageName
                )
                val rotationAngle by animateFloatAsState(
                    targetValue = if (currentCategory.isExpanded) 180f else 0f, label = ""
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Container)
                        .clickable {
                            if (!currentCategory.subItems.isNullOrEmpty()) {
                                categories[index] = currentCategory.copy(
                                    isExpanded = !currentCategory.isExpanded
                                )
                            }else{
                                onItemClick(currentCategory.id)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .height((conf.screenHeightDp * 0.1f).dp)
                                .shadow(2.dp, RoundedCornerShape(8.dp))
                                .scale(1.1f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        SoChicText(
                            text = currentCategory.name ?: "",
                            fontFamily = PoppinsMedium,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 8.dp)
                        )
                    }
                    if (!currentCategory.subItems.isNullOrEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_down),
                            contentDescription = "",
                            tint = TextColor,
                            modifier = Modifier
                                .padding(end = 32.dp)
                                .size(16.dp)
                                .graphicsLayer {
                                    rotationZ = rotationAngle // Apply rotation animation
                                }
                        )
                    }
                }
                if (currentCategory.isExpanded && currentCategory.subItems != null) {
                    currentCategory.subItems.forEachIndexed { index, categoryItem ->
                        SubItem(
                            categoryItem.name,
                            modifier = Modifier.then(
                                if (index == 0) {
                                    Modifier.clip(
                                        RoundedCornerShape(
                                            topEnd = 8.dp,
                                            topStart = 8.dp
                                        )
                                    )
                                } else {
                                    Modifier.clip(
                                        RoundedCornerShape(
                                            bottomEnd = 8.dp,
                                            bottomStart = 8.dp
                                        )
                                    )
                                }
                            ).clickable {
                                onItemClick(categoryItem.id)
                            }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun SubItem(name: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Container),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_circled_arrow_right),
            contentDescription = "",
            tint = TextColor,
            modifier = Modifier
                .padding(8.dp)
                .size(16.dp)
        )
        SoChicText(
            text = name,
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CategoriesLight() {
    EcommerceComposeTheme {
        CategoriesRoute(
            uiState = CategoriesContract.UiState(),
            navigateToProducts = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoriesDark() {
    EcommerceComposeTheme {
        CategoriesRoute(
            uiState = CategoriesContract.UiState(),
            navigateToProducts = {}
        )
    }
}