package com.crazylegend.kotlinextensions.bundle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.AnimRes
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.packageutils.buildIsLollipopAndUp
import java.lang.Exception
import java.io.Serializable


/**
 * Created by Hristijan on 3/6/19 to long live and prosper !
 */




fun Bundle.withCustomAnimation(
    context: Context,
    @AnimRes enterResId: Int,
    @AnimRes exitResId: Int
) {
    this with ActivityOptions.makeCustomAnimation(
        context,
        enterResId, exitResId
    ).toBundle()
}

fun Bundle.withSlideIn(context: Context) = withCustomAnimation(
    context,
    R.anim.slide_in_right, R.anim.fade_out
)

fun Bundle.withSlideOut(context: Context) = withCustomAnimation(
    context,
    R.anim.fade_in, R.anim.abc_slide_out_top
)

fun Bundle.withFade(context: Context) = withCustomAnimation(
    context,
    android.R.anim.fade_in, android.R.anim.fade_out
)

/**
* Similar to [Bundle.putAll], but checks for a null insert and returns the parent bundle
*/
infix fun Bundle.with(bundle: Bundle?): Bundle {
    if (bundle != null) putAll(bundle)
    return this
}

/**
 * Saves all bundle args based on their respective types.
 *
 */
fun bundleOf(vararg params: kotlin.Pair<String, Any?>): Bundle {
    val b = Bundle()
    for (p in params) {
        val (k, v) = p
        when (v) {
            null -> b.putSerializable(k, null)
            is Boolean -> b.putBoolean(k, v)
            is Byte -> b.putByte(k, v)
            is Char -> b.putChar(k, v)
            is Short -> b.putShort(k, v)
            is Int -> b.putInt(k, v)
            is Long -> b.putLong(k, v)
            is Float -> b.putFloat(k, v)
            is Double -> b.putDouble(k, v)
            is String -> b.putString(k, v)
            is CharSequence -> b.putCharSequence(k, v)
            is Parcelable -> b.putParcelable(k, v)
            is Serializable -> b.putSerializable(k, v)
            is BooleanArray -> b.putBooleanArray(k, v)
            is ByteArray -> b.putByteArray(k, v)
            is CharArray -> b.putCharArray(k, v)
            is DoubleArray -> b.putDoubleArray(k, v)
            is FloatArray -> b.putFloatArray(k, v)
            is IntArray -> b.putIntArray(k, v)
            is LongArray -> b.putLongArray(k, v)
            is Array<*> -> {
                @Suppress("UNCHECKED_CAST")
                when {
                    v.isArrayOf<Parcelable>() -> b.putParcelableArray(k, v as Array<out Parcelable>)
                    v.isArrayOf<CharSequence>() -> b.putCharSequenceArray(k, v as Array<out CharSequence>)
                    v.isArrayOf<String>() -> b.putStringArray(k, v as Array<out String>)
                    else -> throw Exception("Unsupported bundle component (${v.javaClass})")
                }
            }
            is ShortArray -> b.putShortArray(k, v)
            is Bundle -> b.putBundle(k, v)
            else -> throw Exception("Unsupported bundle component (${v.javaClass})")
        }
    }
    return b
}

/**
 * Adds transition bundle if context is activity and build is lollipop+
 */
@SuppressLint("NewApi")
fun Bundle.withSceneTransitionAnimation(context: Context) {
    if (context !is Activity || !buildIsLollipopAndUp) return
    val options = ActivityOptions.makeSceneTransitionAnimation(context)
    putAll(options.toBundle())
}


/**
 * Extract bundle and returs string
 */
@JvmOverloads
fun Bundle.extractBundle(keyValSeparator: String = ": ", newSetSeparator: String = "\n"): String {
    val keyvals = StringBuilder()
    keySet().forEach {
        keyvals.append(it).append(keyValSeparator).append(get(it)?.toString()
            ?: "null etnry")
            .append(newSetSeparator)
    }
    return keyvals.toString()
}

/**
 * Creates bundle to map
 */
fun Bundle.toMap(): Map<String, String> {
    val map = HashMap<String, String>()
    for (key in keySet()) {
        map.put(key, get(key)?.toString() ?: "Null Entry")
    }
    return map
}

operator fun Bundle.contains(key: String): Boolean
        = containsKey(key)


inline fun <reified T> Bundle.obtain(key: String, noinline converter: ((Any?) -> T?) = { it as? T }): T?
        = converter(this[key])
