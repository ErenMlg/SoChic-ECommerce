package com.softcross.ecommercecompose.common.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan

fun String.toStrikeThroughText(): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(
        StrikethroughSpan(), // Üstü çizili efekt
        0,
        this.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}