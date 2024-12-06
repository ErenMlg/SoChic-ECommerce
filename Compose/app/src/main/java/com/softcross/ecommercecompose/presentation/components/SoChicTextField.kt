package com.softcross.ecommercecompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.ecommercecompose.presentation.theme.HintColor
import com.softcross.ecommercecompose.presentation.theme.LightContainer
import com.softcross.ecommercecompose.presentation.theme.PoppinsLight

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    givenValue: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit) = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = HintColor,
        backgroundColor = HintColor.copy(alpha = 0.3f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = givenValue, onValueChange = onValueChange,
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(0.9f)
                .shadow(3.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(LightContainer),
            cursorBrush = SolidColor(HintColor),
            keyboardActions = keyboardActions,
            textStyle = TextStyle(
                color = HintColor,
                fontSize = 16.sp,
                fontFamily = PoppinsLight,
                textAlign = TextAlign.Start,
            ),
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .fillMaxWidth()
                ) {
                    if (givenValue.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontFamily = PoppinsLight,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}