package com.softcross.ecommercecompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.data.model.product.Product
import com.softcross.ecommercecompose.presentation.theme.Container
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.PoppinsBold
import com.softcross.ecommercecompose.presentation.theme.PoppinsLight

@Composable
fun SoChicProduct(
    currentProduct: Product,
    onClick: (String) -> Unit
) {
    val conf = LocalConfiguration.current
    Column(
        modifier = Modifier
            .clickable { onClick(currentProduct.id) }
            .padding(8.dp)
            .height((conf.screenHeightDp * 0.4f).dp)
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Container),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            SoChicCoilImage(
                model = currentProduct.images.first(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height((conf.screenHeightDp * 0.3f).dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .shadow(2.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            )
            SoChicText(
                text = currentProduct.name,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 3,
                modifier = Modifier.padding(end = 8.dp, start = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SoChicText(
                text = currentProduct.discountedPrice,
                fontFamily = PoppinsBold,
                fontSize = 14.sp
            )
            if (currentProduct.price != currentProduct.discountedPrice){
                SoChicText(
                    text = currentProduct.price,
                    color = HintColor,
                    fontSize = 12.sp,
                    fontFamily = PoppinsLight,
                    modifier = Modifier.padding(start = 8.dp),
                    textDecoration = TextDecoration.LineThrough
                )
            }
        }
    }
}