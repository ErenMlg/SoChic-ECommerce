package com.softcross.ecommerce.presentation.components

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.sqrt

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val matrix = Matrix()
    private val matrixValues = FloatArray(9)

    private var lastX = 0f
    private var lastY = 0f
    private var lastDistance = 0f

    private var isDragging = false
    private var isZooming = false

    private val minScale = 1f
    private val maxScale = 5f

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initializeImageBounds()
    }

    // Resim boyutlarına göre görüntüyü ayarla
    private fun initializeImageBounds() {
        val drawable = drawable ?: return
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()

        val scale = minOf(viewWidth / drawableWidth, viewHeight / drawableHeight)
        val dx = (viewWidth - drawableWidth * scale) / 2f
        val dy = (viewHeight - drawableHeight * scale) / 2f

        matrix.setScale(scale, scale)
        matrix.postTranslate(dx, dy)
        imageMatrix = matrix
    }

    // Dokunma olaylarını işle
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isDragging = false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    lastDistance = calculateDistance(event)
                    isZooming = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isZooming && event.pointerCount == 2) {
                    val pivotX = (event.getX(0) + event.getX(1)) / 2
                    val pivotY = (event.getY(0) + event.getY(1)) / 2
                    val newDistance = calculateDistance(event)
                    val scaleFactor = newDistance / lastDistance
                    zoom(scaleFactor, pivotX, pivotY)
                    lastDistance = newDistance
                } else if (!isZooming && event.pointerCount == 1) {
                    val dx = event.x - lastX
                    val dy = event.y - lastY

                    if (!isDragging) {
                        isDragging = sqrt(dx * dx + dy * dy) >= 10f
                    }

                    if (isDragging) {
                        translate(dx, dy)
                    }

                    lastX = event.x
                    lastY = event.y
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                isZooming = false
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                isZooming = false
                constrainMatrix()
            }
        }
        return true
    }

    // İki parmak arasındaki mesafeyi hesapla
    private fun calculateDistance(event: MotionEvent): Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return sqrt(dx * dx + dy * dy)
    }

    // Yakınlaştır
    private fun zoom(scaleFactor: Float, pivotX: Float, pivotY: Float) {
        val currentScale = getCurrentScale()
        if (currentScale * scaleFactor in minScale..maxScale) {
            matrix.postScale(scaleFactor, scaleFactor, pivotX, pivotY)
            constrainMatrix()
            imageMatrix = matrix
        }
    }

    // Taşı
    private fun translate(dx: Float, dy: Float) {
        matrix.postTranslate(dx, dy)
        constrainMatrix()
        imageMatrix = matrix
    }

    // Matrisi sınırla
    private fun constrainMatrix() {
        val drawable = drawable ?: return
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()

        matrix.getValues(matrixValues)

        val currentScale = matrixValues[Matrix.MSCALE_X]
        val scaledWidth = drawableWidth * currentScale
        val scaledHeight = drawableHeight * currentScale

        // Pozisyonları hesapla
        val currentX = matrixValues[Matrix.MTRANS_X]
        val currentY = matrixValues[Matrix.MTRANS_Y]

        var dx = 0f
        var dy = 0f

        // Yatay sınırları kontrol et
        if (scaledWidth > viewWidth) {
            if (currentX > 0) dx = -currentX
            else if (currentX + scaledWidth < viewWidth) dx = viewWidth - (currentX + scaledWidth)
        } else {
            dx = (viewWidth - scaledWidth) / 2 - currentX
        }

        // Dikey sınırları kontrol et
        if (scaledHeight > viewHeight) {
            if (currentY > 0) dy = -currentY
            else if (currentY + scaledHeight < viewHeight) dy = viewHeight - (currentY + scaledHeight)
        } else {
            dy = (viewHeight - scaledHeight) / 2 - currentY
        }

        // Sadece gerekli düzeltmeleri uygula
        if (dx != 0f || dy != 0f) {
            matrix.postTranslate(dx, dy)
        }
    }

    // Mevcut ölçeği al
    private fun getCurrentScale(): Float {
        matrix.getValues(matrixValues)
        return matrixValues[Matrix.MSCALE_X]
    }
}
