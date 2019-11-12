package com.crazylegend.kotlinextensions.resources

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.DimenRes


/**
 * Created by crazy on 11/12/19 to long live and prosper !
 */



/**
 * Alternative to Resources.getDimension() for values that are TYPE_FLOAT.
 */
fun Resources.getFloat(@DimenRes resId: Int): Float {
    val outValue = TypedValue()
    getValue(resId, outValue, true)
    return outValue.float
}

