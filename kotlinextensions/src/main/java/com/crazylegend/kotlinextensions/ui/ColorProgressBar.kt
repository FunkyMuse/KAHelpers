package com.crazylegend.kotlinextensions.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.views.tint


/**
 * Created by hristijan on 4/15/19 to long live and prosper !
 */

/**
 * Class to Render ProgressBar with some Specific Color with the ease
 */

class ColorProgressBar : ProgressBar {
    /**
     * Context Constructor
     */
    constructor(context: Context) : super(context)
    /**
     * Context,AttributeSet Constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }
    /**
     * Context,AttributeSet,Int Constructor
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorProgressBar)
        val color = attributes.getColor(R.styleable.ColorProgressBar_progressColor, Color.WHITE)
        attributes.recycle()
        setColor(color)
    }

    /**
     * Set Color Dynamically
     */
    fun setColor(@ColorInt color: Int): ColorProgressBar {
        indeterminateDrawable.tint(color)
        return this
    }
}