package com.softcross.ecommercecompose.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.softcross.ecommercecompose.R
import com.softcross.ecommercecompose.presentation.theme.EcommerceComposeTheme

@Composable
fun SoChicError(modifier: Modifier = Modifier, errorMessage: String = "Error") {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            clipTextToBoundingBox = true,
            maintainOriginalImageBounds = true,
            modifier = modifier,
            composition = composition,
            progress = { progress },
        )
        SoChicText(text = errorMessage)
    }
}

@Composable
fun SoChicLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        val progress by animateLottieCompositionAsState(
            composition = composition
        )
        LottieAnimation(
            clipTextToBoundingBox = true,
            maintainOriginalImageBounds = true,
            modifier = modifier,
            composition = composition,
            progress = { progress },
        )
        SoChicText(text = "Loading...")
    }
}

@Preview
@Composable
private fun SDSGfhdgjf() {
    EcommerceComposeTheme {
        SoChicError()
    }
}