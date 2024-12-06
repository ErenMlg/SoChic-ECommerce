package com.softcross.ecommerce.common.extensions

import android.view.View

fun View.setInvisible() {
    this.visibility = View.GONE
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}