package com.funkymuse.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView


class NonScrollableListView : ListView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, measureSpec)
        val params = layoutParams
        params.height = measuredHeight
    }


}