package com.crazylegend.kotlinextensions.views

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import com.crazylegend.kotlinextensions.context.colorWithOpacity
import com.crazylegend.kotlinextensions.context.drawable
import com.crazylegend.kotlinextensions.context.inputManager


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


private fun ViewGroup.enableDisableChildren(enable: Boolean): ViewGroup = apply {
    (0 until childCount).forEach {
        when (val view = getChildAt(it)) {
            is ViewGroup -> view.enableDisableChildren(enable)
            else -> if (enable) view.enable() else view.disable()
        }
    }
}

fun ViewGroup.disableChildren() = enableDisableChildren(false)
fun ViewGroup.enableChildren() = enableDisableChildren(true)

fun View.hideIme() {
    val imm = context.inputManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Get color from resources with alpha
 */
@ColorInt
@Deprecated(message = "Use new [colorWithOpacity] extension", replaceWith = ReplaceWith("colorWithOpacity(res, alphaPercent)"))
fun View.colorWithAlpha(@ColorRes res: Int, @IntRange(from = 0, to = 100) alphaPercent: Int): Int {
    return colorWithOpacity(res, alphaPercent)
}

/**
 * Get color from resources with applied [opacity]
 */
@ColorInt
fun View.colorWithOpacity(@ColorRes res: Int, @IntRange(from = 0, to = 100) opacity: Int): Int {
    return context.colorWithOpacity(res, opacity)
}


/**
 * Get bitmap representation of view
 */
fun View.asBitmap(): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    layout(left, top, right, bottom)
    draw(c)
    return b
}



/**
 * View artificial attribute that sets compound left drawable
 */
var TextView.drawableLeft: Int
    get() = throw IllegalAccessException("Property drawableLeft only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(context.drawable(value), drawables[1], drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound right drawable
 */
var TextView.drawableRight: Int
    get() = throw IllegalAccessException("Property drawableRight only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], context.drawable(value), drawables[3])
    }

/**
 * View artificial attribute that sets compound top drawable
 */
var TextView.drawableTop: Int
    get() = throw IllegalAccessException("Property drawableTop only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], context.drawable(value), drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound bottom drawable
 */
var TextView.drawableBottom: Int
    get() = throw IllegalAccessException("Property drawableBottom only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], context.drawable(value))
    }



/**
 * Convert this Drawable to Bitmap representation. Should take care of every Drawable type
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    Canvas(bitmap).apply {
        setBounds(0, 0, width, height)
        draw(this)
    }
    return bitmap
}


/**
 * Usage drawable.colorFilterCompat(getColorCompat(R.color.colorAccent))
 * @receiver Drawable
 * @param color Int
 */
fun Drawable.colorFilterCompat(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter =
                BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}
