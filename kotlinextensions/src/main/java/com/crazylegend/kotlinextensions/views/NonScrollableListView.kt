package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

class NonScrollableListView : ListView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val params = layoutParams
        params.height = measuredHeight
    }


}