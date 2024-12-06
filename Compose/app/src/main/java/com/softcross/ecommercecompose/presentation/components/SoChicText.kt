package com.softcross.ecommercecompose.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.presentation.theme.Poppins
import com.softcross.ecommercecompose.presentation.theme.PoppinsMedium
import com.softcross.ecommercecompose.presentation.theme.Surface
import com.softcross.ecommercecompose.presentation.theme.TextColor

@Composable
fun SoChicText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    fontFamily: FontFamily = Poppins,
    color: Color = Surface,
    fontSize: TextUnit = 16.sp,
    maxLines : Int = Int.MAX_VALUE,
    textDecoration: TextDecoration? = null,
    lineHeight: TextUnit = 16.sp
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign,
        modifier = modifier,
        maxLines = maxLines,
        textDecoration = textDecoration,
        overflow = TextOverflow.Ellipsis,
        lineHeight = lineHeight
    )
}