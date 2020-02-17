package com.crazylegend.kotlinextensions.ui

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

class WrapContentHeightViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureSpec: Int

        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }

        measureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, measureSpec)
    }
}
