package com.crazylegend.customviews.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.crazylegend.customviews.R


/**
 * Created by hristijan on 4/15/19 to long live and prosper !
 */
/**
 * Aspect Ratio Frame Layout, Here to Set the Width Height Based on Aspect Ratio
 */
class AspectRatioFrameLayout : FrameLayout {
    private var xRatio = DEFAULT_XRATIO
    private var yRatio = DEFAULT_YRATIO

    /**
     * context Constructor
     */
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    /**
     * context,attr Constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    /**
     * context,attr,defStyle Constructor
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout, 0, 0)
        try {
            xRatio = a.getInt(R.styleable.AspectRatioFrameLayout_xRatio, DEFAULT_XRATIO)
            yRatio = a.getInt(R.styleable.AspectRatioFrameLayout_yRatio, DEFAULT_YRATIO)
        } finally {
            a.recycle()
        }
    }

    /**
     * Method Which Runs on Measure time
     *
     * We OverRidden it Because of setting the width height based on the aspect ratio
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY && (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED)) {
            setMeasuredDimension(widthSize, (widthSize.toDouble() / xRatio * yRatio).toInt())
        } else if (heightMode == MeasureSpec.EXACTLY && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED)) {
            setMeasuredDimension((heightSize.toDouble() / yRatio * xRatio).toInt(), heightSize)
        } else {
            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        }
    }

    companion object {

        private val DEFAULT_XRATIO = 1

        private val DEFAULT_YRATIO = 1
    }
}