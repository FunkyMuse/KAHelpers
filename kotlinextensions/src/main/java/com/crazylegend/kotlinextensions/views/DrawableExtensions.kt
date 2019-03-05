package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Handler
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.kotlinextensions.color.adjustAlpha


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */
/**
 * Converts a Bitmap to a Drawable
 */
fun Context.bitmapToDrawable(bitmap: Bitmap?): Drawable? = BitmapDrawable(this.resources, bitmap)

/**
 * Converts this Bitmap to a Drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable? = BitmapDrawable(context.resources, this)

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

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun createSimpleRippleDrawable(@ColorInt foregroundColor: Int, @ColorInt backgroundColor: Int): RippleDrawable {
    val states = ColorStateList(arrayOf(intArrayOf()), intArrayOf(foregroundColor))
    val content = ColorDrawable(backgroundColor)
    val mask = ColorDrawable(foregroundColor.adjustAlpha(0.16f))
    return RippleDrawable(states, content, mask)
}

inline fun RippleDrawable.forceAnimation(timeInMs: Long = 200) {
    state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    val handler = Handler()
    handler.postDelayed({ state = intArrayOf() }, timeInMs)
}

/**
 * Sets the bounds of the drawable to be centered around the location [centerX], [centerY] based on minimum size.
 */
inline fun Drawable.setBoundsCentered(centerX: Float, centerY: Float) = setBoundsCentered(centerX.toInt(), centerY.toInt())

/**
 * Sets the bounds of the drawable to be centered around the location [centerX], [centerY] based on minimum size.
 */
fun Drawable.setBoundsCentered(centerX: Int, centerY: Int) {
    val left = centerX - minimumWidth / 2
    val top = centerY - minimumHeight / 2
    setBounds(left, top, left + minimumWidth, top + minimumHeight)
}