package com.softcross.ecommercecompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.softcross.ecommercecompose.presentation.theme.Background
import com.softcross.ecommercecompose.presentation.theme.HintColor

@Composable
fun PagerIndicator(currentPage:Int, modifier: Modifier, size:Int) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        items(size) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (currentPage == index) HintColor else Background
                    )
            )
        }
    }
}