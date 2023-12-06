package com.tunghoang.object_detection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View


class DrawView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var boundryPaint: Paint = Paint()
    var textPaint: Paint

    private var rect: Rect = Rect()
    private var text: String = ""
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

    init {
        boundryPaint.color = Color.YELLOW
        boundryPaint.strokeWidth = 10f
        boundryPaint.style = Paint.Style.STROKE
        textPaint = Paint()
        textPaint.color = Color.YELLOW
        textPaint.strokeWidth = 50f
        textPaint.textSize = 32f
        textPaint.style = Paint.Style.FILL
    }

    fun setData(rect: Rect, text: String, imageWidth: Int, imageHeight: Int) {
        this.rect = rect
        this.text = text
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val top = (height - imageWidth) / 2f
        Log.d("DrawLog", "top: $top")
        Log.d("DrawLog", "imageWidth: $imageWidth")
        Log.d("DrawLog", "imageHeight: $imageHeight")
        Log.d("DrawLog", "getWidth: $width")
        Log.d("DrawLog", "getHeight: $height")
        Log.d("DrawLog", "getHeight: =============")
        canvas.drawText(text, rect.centerX().toFloat(), rect.centerY().toFloat(), textPaint)
        canvas.drawRect(rect.left.toFloat(), rect.top + top, rect.right.toFloat(), rect.bottom + top, boundryPaint)
    }
}