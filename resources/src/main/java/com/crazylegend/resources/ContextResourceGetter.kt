package com.crazylegend.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */


/**
 * Get color from resources
 */
fun Context.compatColor(@ColorRes colorInt: Int): Int =
    ContextCompat.getColor(this, colorInt)

/**
 * Get drawable from resources
 */
fun Context.compatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    try {
        ContextCompat.getDrawable(this, drawableRes)
    } catch (e: Exception) {
        AppCompatResources.getDrawable(this, drawableRes)
    }