package com.funkymuse.resources

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import com.funkymuse.common.string.toHtmlSpan

/**
 * Alternative to Resources.getDimension() for values that are TYPE_FLOAT.
 */
fun Resources.getFloat(@DimenRes resId: Int): Float {
    val outValue = TypedValue()
    getValue(resId, outValue, true)
    return outValue.float
}

fun Resources.getHtmlSpannedString(@StringRes id: Int): Spanned = getString(id).toHtmlSpan()

fun Resources.getHtmlSpannedString(@StringRes id: Int, vararg formatArgs: Any): Spanned = getString(id, *formatArgs).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int): Spanned = getQuantityString(id, quantity).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): Spanned = getQuantityString(id, quantity, *formatArgs).toHtmlSpan()


fun TypedArray.getDrawableCompat(context: Context, @StyleableRes id: Int): Drawable? {
    val resource = getResourceId(id, 0)
    if (resource != 0) {
        return AppCompatResources.getDrawable(context, resource)
    }
    return null
}