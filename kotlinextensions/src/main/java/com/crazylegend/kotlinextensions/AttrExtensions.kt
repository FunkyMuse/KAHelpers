package com.crazylegend.kotlinextensions

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

/**
 * Get a color from the attribute theme
 *
 * @param attributeColor the attribute color, ex R.attr.colorPrimary
 * @return the color, or [Color.TRANSPARENT] if failed to resolve
 */
@ColorInt
fun Context.themeAttrColor(@AttrRes attributeColor: Int): Int {
    val attrs = intArrayOf(attributeColor)
    val ta = obtainStyledAttributes(attrs)
    val color = ta.getColor(0, Color.TRANSPARENT)
    ta.recycle()
    return color
}


/**
 * Get a drawable from the attribute theme
 *
 * @param attributeDrawable the attribute drawable, ex R.attr.selectableItemBackground
 * @return the drawable, if it exists in the theme context
 */
fun Context.themeAttrDrawable(@AttrRes attributeDrawable: Int): Drawable? {
    val attrs = intArrayOf(attributeDrawable)
    val ta = obtainStyledAttributes(attrs)
    val drawableFromTheme = ta.getDrawable(0)
    ta.recycle()
    return drawableFromTheme
}

/**
 * Get a dimen from the attribute theme
 *
 * @param attributeDimen the attribute dimen, ex R.attr.actionBarSize
 * @return the dimen pixel size, if it exists in the theme context. Otherwise, -1
 */
fun Context.themeAttrDimen(@AttrRes attributeDimen: Int): Float {
    val tv = TypedValue()

    var value = -1
    if (theme.resolveAttribute(attributeDimen, tv, true)) {
        value = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return value.toFloat()
}
