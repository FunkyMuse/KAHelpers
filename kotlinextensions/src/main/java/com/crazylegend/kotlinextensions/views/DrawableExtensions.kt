package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.color.adjustAlpha


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */
/**
 * Converts a Bitmap to a Drawable
 */
fun Context.bitmapToDrawable(bitmap: Bitmap?): Drawable = BitmapDrawable(this.resources, bitmap)

/**
 * Converts this Bitmap to a Drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable = BitmapDrawable(context.resources, this)

/**
 * Wrap the color into a state and tint the drawable
 */
fun Drawable.tint(@ColorInt color: Int): Drawable = tint(ColorStateList.valueOf(color))

/**
 * Tint the drawable with a given color state list
 */
fun Drawable.tint(state: ColorStateList): Drawable {
    val drawable = DrawableCompat.wrap(mutate())
    DrawableCompat.setTintList(drawable, state)
    return drawable
}


fun createSimpleRippleDrawable(@ColorInt foregroundColor: Int, @ColorInt backgroundColor: Int): RippleDrawable {
    val states = ColorStateList(arrayOf(intArrayOf()), intArrayOf(foregroundColor))
    val content = ColorDrawable(backgroundColor)
    val mask = ColorDrawable(foregroundColor.adjustAlpha(0.16f))
    return RippleDrawable(states, content, mask)
}

fun RippleDrawable.forceAnimation(timeInMs: Long = 200, looper: Looper) {
    state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    val handler = Handler(looper)
    handler.postDelayed({ state = intArrayOf() }, timeInMs)
}

/**
 * Sets the bounds of the drawable to be centered around the location [centerX], [centerY] based on minimum size.
 */
fun Drawable.setBoundsCentered(centerX: Float, centerY: Float) = setBoundsCentered(centerX.toInt(), centerY.toInt())

/**
 * Sets the bounds of the drawable to be centered around the location [centerX], [centerY] based on minimum size.
 */
fun Drawable.setBoundsCentered(centerX: Int, centerY: Int) {
    val left = centerX - minimumWidth / 2
    val top = centerY - minimumHeight / 2
    setBounds(left, top, left + minimumWidth, top + minimumHeight)
}

/**
 * Uses compatibility library to create a tinted a drawable. Supports all important versions of Android.
 * @param drawable Drawable to tint
 * *
 * @param color    Color to tint to, *not* R.color.whatever, must be resolved
 * *
 * @param mode     Mode of tinting to use
 * *
 * @return A tinted Drawable. Wraps the Drawable in a new class - instanceof will NOT match the old type
 */
@JvmOverloads
fun createTintedDrawable(drawable: Drawable?, @ColorInt color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN): Drawable? {
    if (drawable == null) {
        return null
    }

    val wrappedDrawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrappedDrawable, color)
    DrawableCompat.setTintMode(wrappedDrawable, mode)
    return wrappedDrawable
}

/**
 * Uses compatibility library to create a tinted a drawable. Supports all important versions of Android.
 * @param drawable Drawable to tint
 * *
 * @param color    Color to tint to
 * *
 * @param mode     Mode of tinting to use
 * *
 * @return A tinted Drawable. Wraps the Drawable in a new class - instanceof will NOT match the old type
 */
fun createTintedDrawable(drawable: Drawable?, color: ColorStateList, mode: PorterDuff.Mode): Drawable? {
    if (drawable == null) {
        return null
    }

    val wrappedDrawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTintList(wrappedDrawable, color)
    DrawableCompat.setTintMode(wrappedDrawable, mode)
    return wrappedDrawable
}

var Drawable.tint: Int
    @ColorInt get() = tint
    @ColorInt set(value) {
        if (Build.VERSION.SDK_INT >= 21) setTint(value)
        else DrawableCompat.setTint(DrawableCompat.wrap(this), value)
    }


val LayerDrawable.layers: List<Drawable>
    get() = (0 until numberOfLayers).map { getDrawable(it) }

fun Drawable.setSize(width: Int, height: Int) {
    bounds = Rect(0, 0, width, height)
}



