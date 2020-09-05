package com.crazylegend.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
<com.example.DividerView
android:layout_width="1dp"
android:layout_height="fill_parent"
android:layerType="software"
custom:color="@color/grey"
custom:orientation="vertical"
custom:dashLength="1dp"
custom:dashGap="1dp"
custom:dashThickness="1dp" />
 */

/**
 * Created by crazy on 3/14/19 to long live and prosper !
 */
open class DividerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val mPaint: Paint
    private var orientation: Int = 0

    init {
        val dashGap: Int
        val dashLength: Int
        val dashThickness: Int
        val color: Int

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DividerView, 0, 0)

        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DividerView_dashGap, 5)
            dashLength = a.getDimensionPixelSize(R.styleable.DividerView_dashLength, 5)
            dashThickness = a.getDimensionPixelSize(R.styleable.DividerView_dashThickness, 3)
            color = a.getColor(R.styleable.DividerView_color, -0x1000000)
            orientation = a.getInt(R.styleable.DividerView_orientation, ORIENTATION_HORIZONTAL)
        } finally {
            a.recycle()
        }

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = dashThickness.toFloat()
        mPaint.pathEffect = DashPathEffect(floatArrayOf(dashLength.toFloat(), dashGap.toFloat()), 0f)
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            val center = height * .5f
            canvas.drawLine(0f, center, width.toFloat(), center, mPaint)
        } else {
            val center = width * .5f
            canvas.drawLine(center, 0f, center, height.toFloat(), mPaint)
        }
    }

    companion object {
        const val ORIENTATION_HORIZONTAL = 0
        const val ORIENTATION_VERTICAL = 1
    }
}