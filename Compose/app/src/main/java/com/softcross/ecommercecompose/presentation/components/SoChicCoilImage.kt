package com.softcross.ecommercecompose.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.presentation.theme.Container
import com.softcross.ecommercecompose.presentation.theme.TextColor

@Composable
fun SoChicCoilImage(
    model: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorIcon: Int = R.drawable.icon_error
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.background(Container),
        alignment = alignment,
        contentScale = contentScale,
        loading = {
            Image(
                painter = painterResource(id = R.drawable.icon_loading),
                contentScale = FixedScale(2f),
                colorFilter = ColorFilter.tint(TextColor),
                contentDescription = "Loading"
            )
        },
        error = {
            Image(
                painter = painterResource(id = errorIcon),
                contentScale = FixedScale(2f),
                colorFilter = ColorFilter.tint(TextColor),
                contentDescription = "Error"
            )
        }
    )
}