package com.crazylegend.kotlinextensions.viewpager

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes


/**
 * Created by hristijan on 6/7/19 to long live and prosper !
 */


inline fun TextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

inline fun View.px(@DimenRes rid: Int): Int {
    return this.context.resources.getDimensionPixelOffset(rid)
}