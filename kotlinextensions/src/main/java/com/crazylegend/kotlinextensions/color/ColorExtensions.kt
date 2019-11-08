package com.crazylegend.kotlinextensions.color

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.context.drawable
import com.crazylegend.kotlinextensions.context.resolveColor
import com.crazylegend.kotlinextensions.numbers.round
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */


/**
 * Return the color with 0xFF opacity.
 * E.g., `0xabcdef` will be translated to `0xFFabcdef`.
 */
val Int.opaque: Int
    get() = this or 0xff000000.toInt()

/**
 * Generates a random opaque color
 * Note that this is mainly for testing
 * Should you require this method often, consider
 * rewriting the method and storing the [Random] instance
 * rather than generating one each time
 */
inline val randomColor: Int
    get() {
        val rnd = Random()
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

inline val Int.isColorDark: Boolean
    get() = isColorDark(0.5f)

fun Int.isColorDark(minDarkness: Float): Boolean =
    ((0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255.0) < minDarkness

fun Int.toHexString(withAlpha: Boolean = false, withHexPrefix: Boolean = true): String {
    val hex = if (withAlpha) String.format("#%08X", this)
    else String.format("#%06X", 0xFFFFFF and this)
    return if (withHexPrefix) hex else hex.substring(1)
}

fun Int.toRgbaString(): String =
    "rgba(${Color.red(this)}, ${Color.green(this)}, ${Color.blue(this)}, ${(Color.alpha(this) / 255f).round(3)})"

fun Int.toHSV(): FloatArray {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    return hsv
}

inline val Int.isColorOpaque: Boolean
    get() = Color.alpha(this) == 255

fun FloatArray.toColor(): Int = Color.HSVToColor(this)

fun Int.isColorVisibleOn(
    @ColorInt color: Int,
    @androidx.annotation.IntRange(from = 0L, to = 255L) delta: Int = 25,
    @androidx.annotation.IntRange(from = 0L, to = 255L) minAlpha: Int = 50
): Boolean =
    if (Color.alpha(this) < minAlpha) false
    else !(abs(Color.red(this) - Color.red(color)) < delta &&
            abs(Color.green(this) - Color.green(color)) < delta &&
            abs(Color.blue(this) - Color.blue(color)) < delta)

@ColorInt
fun Context.getDisabledColor(): Int {
    val primaryColor = resolveColor(android.R.attr.textColorPrimary)
    val disabledColor = if (primaryColor.isColorDark) Color.BLACK else Color.WHITE
    return disabledColor.adjustAlpha(0.3f)
}

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}

inline val Int.isColorTransparent: Boolean
    get() = Color.alpha(this) != 255

@ColorInt
fun Int.blendWith(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
    val inverseRatio = 1f - ratio
    val a = Color.alpha(this) * inverseRatio + Color.alpha(color) * ratio
    val r = Color.red(this) * inverseRatio + Color.red(color) * ratio
    val g = Color.green(this) * inverseRatio + Color.green(color) * ratio
    val b = Color.blue(this) * inverseRatio + Color.blue(color) * ratio
    return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
}

@ColorInt
fun Int.withAlpha(@androidx.annotation.IntRange(from = 0, to = 0xFF) alpha: Int): Int =
    this and 0x00FFFFFF or (alpha shl 24)

@ColorInt
fun Int.withMinAlpha(@androidx.annotation.IntRange(from = 0, to = 0xFF)alpha: Int): Int =
    withAlpha(alpha.coerceAtLeast(this ushr 24))

@ColorInt
private inline fun Int.colorFactor(rgbFactor: (Int) -> Float): Int {
    val (red, green, blue) = intArrayOf(Color.red(this), Color.green(this), Color.blue(this))
        .map { rgbFactor(it).toInt() }
    return Color.argb(Color.alpha(this), red, green, blue)
}

@ColorInt
fun Int.lighten(@FloatRange(from = 0.0, to = 1.0) factor: Float = 0.1f): Int = colorFactor {
    (it * (1f - factor) + 255f * factor)
}

@ColorInt
fun Int.darken(@FloatRange(from = 0.0, to = 1.0) factor: Float = 0.1f): Int = colorFactor {
    it * (1f - factor)
}

@ColorInt
fun Int.colorToBackground(@FloatRange(from = 0.0, to = 1.0) factor: Float = 0.1f): Int =
    if (isColorDark) darken(factor) else lighten(factor)

@ColorInt
fun Int.colorToForeground(@FloatRange(from = 0.0, to = 1.0) factor: Float = 0.1f): Int =
    if (isColorDark) lighten(factor) else darken(factor)

fun String.toColor(): Int {
    val toParse: String = if (startsWith("#") && length == 4)
        "#${this[1]}${this[1]}${this[2]}${this[2]}${this[3]}${this[3]}"
    else
        this
    return Color.parseColor(toParse)
}

//Get ColorStateList
fun Context.colorStateList(@ColorInt color: Int): ColorStateList {
    val disabledColor = color.adjustAlpha(0.3f)
    return ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_enabled, -android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_checked)
        ),
        intArrayOf(color.adjustAlpha(0.8f), color, disabledColor, disabledColor)
    )
}


@SuppressLint("PrivateResource")
fun RadioButton.tint(colors: ColorStateList) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        buttonTintList = colors
    } else {
        val radioDrawable = context.drawable(R.drawable.abc_btn_radio_material)
        val d = radioDrawable?.let { DrawableCompat.wrap(it) }
        d?.let { DrawableCompat.setTintList(it, colors) }
        buttonDrawable = d
    }
}

fun RadioButton.tint(@ColorInt color: Int) = tint(context.colorStateList(color))

@SuppressLint("PrivateResource")
fun CheckBox.tint(colors: ColorStateList) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        buttonTintList = colors
    } else {
        val checkDrawable = context.drawable(R.drawable.abc_btn_check_material)
        val drawable = checkDrawable?.let { DrawableCompat.wrap(it) }
        drawable?.let { DrawableCompat.setTintList(it, colors) }
        buttonDrawable = drawable
    }
}

fun CheckBox.tint(@ColorInt color: Int) = tint(context.colorStateList(color))

fun SeekBar.tint(@ColorInt color: Int) {
    val s1 = ColorStateList.valueOf(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        thumbTintList = s1
        progressTintList = s1
    } else {
        val progressDrawable = DrawableCompat.wrap(progressDrawable)
        this.progressDrawable = progressDrawable
        DrawableCompat.setTintList(progressDrawable, s1)
        val thumbDrawable = DrawableCompat.wrap(thumb)
        DrawableCompat.setTintList(thumbDrawable, s1)
        thumb = thumbDrawable
    }
}

fun ProgressBar.tint(@ColorInt color: Int, skipIndeterminate: Boolean = false) {
    val sl = ColorStateList.valueOf(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        progressTintList = sl
        secondaryProgressTintList = sl
        if (!skipIndeterminate) indeterminateTintList = sl
    } else {
        val mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
        indeterminateDrawable?.setColorFilter(color, mode)
        progressDrawable?.setColorFilter(color, mode)
    }
}

fun Context.textColorStateList(@ColorInt color: Int): ColorStateList {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_focused),
        intArrayOf()
    )
    val colors = intArrayOf(
        resolveColor(R.attr.colorControlNormal),
        resolveColor(R.attr.colorControlNormal),
        color
    )
    return ColorStateList(states, colors)
}

@SuppressLint("RestrictedApi")
fun EditText.tint(@ColorInt color: Int) {
    val editTextColorStateList = context.textColorStateList(color)
    if (this is AppCompatEditText) {
        supportBackgroundTintList = editTextColorStateList
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        backgroundTintList = editTextColorStateList
    }
    tintCursor(color)
}

fun EditText.tintCursor(@ColorInt color: Int) {
    try {
        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(this)
        val clazz = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables: Array<Drawable?> = Array(2) {
            val drawable = context.drawable(mCursorDrawableRes)
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            drawable
        }
        fCursorDrawable.set(editor, drawables)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Toolbar.tint(@ColorInt color: Int, tintTitle: Boolean = true) {
    if (tintTitle) {
        setTitleTextColor(color)
        setSubtitleTextColor(color)
    }
    (0 until childCount).asSequence().forEach { (getChildAt(it) as? ImageButton)?.setColorFilter(color) }
}

/**
 * Returns the color with the new alpha value.
 */
fun Int.colorAlpha(alpha: Int): Int {
    return (this and 0x00FFFFFF) or (alpha shl 24)
}

/**
 * Returns the color with the new alpha value.
 * @param alpha How transparent the color is, from 0 (totally transparent) to 1 (totally opaque).
 */
fun Int.colorAlpha(alpha: Float): Int {
    return (this and 0x00FFFFFF) or ((alpha.coerceIn(0f, 1f) * 0xFF).toInt() shl 24)
}

/**
 * Multiplies the color components (besides alpha), darkening it.
 * @param value A value between 0 (white becomes black) and 1 (no effect).
 */
fun Int.colorMultiply(value: Double): Int {
    return Color.argb(
            Color.alpha(this),
            (Color.red(this) * value).toInt().coerceIn(0, 255),
            (Color.green(this) * value).toInt().coerceIn(0, 255),
            (Color.blue(this) * value).toInt().coerceIn(0, 255)
    )
}

/**
 * Multiplies the color components (besides alpha), darkening it.
 * @param value A value between 0 (white becomes black) and 1 (no effect).
 */
fun Int.colorMultiply(value: Float): Int {
    return Color.argb(
            Color.alpha(this),
            (Color.red(this) * value).toInt().coerceIn(0, 255),
            (Color.green(this) * value).toInt().coerceIn(0, 255),
            (Color.blue(this) * value).toInt().coerceIn(0, 255)
    )
}

/**
 * Adds to the color components (besides alpha), lightening it.
 * @param value A value between 0 (no effect) and 1 (black becomes white).
 */
fun Int.colorAdd(value: Double): Int {
    return Color.argb(
            Color.alpha(this),
            (Color.red(this) + (value * 0xFF).toInt()).coerceIn(0, 255),
            (Color.green(this) + (value * 0xFF).toInt()).coerceIn(0, 255),
            (Color.blue(this) + (value * 0xFF).toInt()).coerceIn(0, 255)
    )
}

/**
 * Adds to the color components (besides alpha), lightening it.
 * @param value A value between 0 (no effect) and 1 (black becomes white).
 */
fun Int.colorAdd(value: Float): Int {
    return Color.argb(
            Color.alpha(this),
            (Color.red(this) + (value * 0xFF).toInt()).coerceIn(0, 255),
            (Color.green(this) + (value * 0xFF).toInt()).coerceIn(0, 255),
            (Color.blue(this) + (value * 0xFF).toInt()).coerceIn(0, 255)
    )
}