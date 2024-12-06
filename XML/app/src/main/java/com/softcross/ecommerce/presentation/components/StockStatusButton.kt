package com.softcross.ecommerce.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.softcross.ecommerce.R

class StockStatusButton @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {

    private var isStockAvailable: Boolean = true
    private var isActive: Boolean = false

    // Stok durumu ve aktiflik durumunu ayarlamak için kullanılır
    fun setStockStatus(isStockAvailable: Boolean) {
        this.isStockAvailable = isStockAvailable
        invalidate()
    }

    // Aktiflik durumunu ayarlamak için kullanılır
    fun setActive() {
        this.isActive = true
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val borderPaint = Paint().apply {
            strokeWidth = 5f // Border width
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        when {
            !isStockAvailable -> {
                drawOutOfStock(canvas)
            }

            isActive -> {
                drawBorder(canvas, borderPaint.apply {
                    color = ContextCompat.getColor(context, R.color.colorGold) // Border color
                })
            }

            else -> {
                drawBorder(canvas, borderPaint.apply {
                    color = ContextCompat.getColor(context, R.color.colorPrimary) // Border color
                })
            }
        }
    }

    // Stokta olmayan ürünler için çizim yapar
    private fun drawOutOfStock(canvas: Canvas) {
        val paint = Paint().apply {
            color =
                ContextCompat.getColor(context, R.color.textColorHint) // Customize the color
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val left = 10f
        val top = 10f
        val right = width - 10f
        val bottom = height - 10f

        canvas.drawLine(left, top, right, bottom, paint)
        canvas.drawLine(left, bottom, right, top, paint)
        drawBorder(canvas, paint)
    }

    // Kenarlık çizimi yapar
    private fun drawBorder(canvas: Canvas, borderPaint: Paint) {
        val borderOffset = 5f
        canvas.drawRoundRect(
            borderOffset,
            borderOffset,
            width - borderOffset,
            height - borderOffset,
            10f,
            10f,
            borderPaint
        )
    }
}
