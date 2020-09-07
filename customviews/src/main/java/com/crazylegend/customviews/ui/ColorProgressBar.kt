package com.crazylegend.customviews.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.customviews.R


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
     * Wrap the color into a state and tint the drawable
     */
    private fun Drawable.tint(@ColorInt color: Int): Drawable = tint(ColorStateList.valueOf(color))

    /**
     * Tint the drawable with a given color state list
     */
    private fun Drawable.tint(state: ColorStateList): Drawable {
        val drawable = DrawableCompat.wrap(mutate())
        DrawableCompat.setTintList(drawable, state)
        return drawable
    }

    /**
     * Set Color Dynamically
     */
    fun setColor(@ColorInt color: Int): ColorProgressBar {
        indeterminateDrawable.tint(color)
        return this
    }
}