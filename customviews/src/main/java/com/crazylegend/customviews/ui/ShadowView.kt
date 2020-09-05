package com.crazylegend.customviews.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import com.crazylegend.customviews.R


/**
 * Created by hristijan on 4/15/19 to long live and prosper !
 */

/**
 * A simple Class to set the Drop Shadow Above or Below any View
 */
class ShadowView : View {
    /**
     * context Constructor
     */
    constructor(context: Context) : super(context)

    /**
     * context,attr Constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttr(context, attrs)
    }

    /**
     * context,attr,defStyle Constructor
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        parseAttr(context, attrs)
    }

    private fun parseAttr(context: Context, attributeSet: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.ShadowView,
                0, 0
        )
        init(
                GradientDrawable.Orientation.values()[a.getInteger(R.styleable.ShadowView_angle, 0)],
                a.getColor(R.styleable.ShadowView_startColor, Color.TRANSPARENT),
                a.getColor(R.styleable.ShadowView_endColor, Color.TRANSPARENT)
        )
        a.recycle()
    }

    private fun init(orientation: GradientDrawable.Orientation, startColor: Int, endColor: Int) {
        val oldBackground = background
        if (oldBackground == null)
            background = GradientDrawable(orientation, intArrayOf(startColor, endColor))
        else if (oldBackground is GradientDrawable) {
            oldBackground.colors = intArrayOf(startColor, endColor)
            oldBackground.orientation = orientation
        }
    }
}