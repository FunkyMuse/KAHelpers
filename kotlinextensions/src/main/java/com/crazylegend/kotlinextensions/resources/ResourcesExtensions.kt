package com.crazylegend.kotlinextensions.resources

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.DimenRes
import java.io.InputStream


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

/**
 * Opens raw input stream for reading
 * @receiver Context
 * @param rawId Int
 * @param inputStream [@kotlin.ExtensionFunctionType] Function1<InputStream, Unit>
 */
fun Context.getRaw(rawId: Int, inputStream: InputStream.() -> Unit) = resources.openRawResource(rawId).use { it.inputStream() }


fun Context.getTypedArray(typedArrayID: Int) = resources.obtainTypedArray(typedArrayID)