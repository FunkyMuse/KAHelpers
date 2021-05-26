package com.crazylegend.kotlinextensions.bundle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.annotation.AnimRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.crazylegend.common.TAG
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.log.log
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


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
        R.anim.slide_in_right, R.anim.fade_out_interpolated
)

fun Bundle.withSlideOut(context: Context) = withCustomAnimation(
        context,
        R.anim.fade_in_interpolated, android.R.anim.slide_out_right
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
fun bundleOfPrimitives(vararg params: Pair<String, Any?>): Bundle {
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
    if (context !is Activity) return
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

operator fun Bundle.contains(key: String): Boolean = containsKey(key)


inline fun <reified T> Bundle.obtain(key: String, noinline converter: ((Any?) -> T?) = { it as? T }): T? = converter(this[key])


inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}


fun Bundle.printBundle() {
    for (key in keySet()) {
        val value = get(key)
        log("${value?.TAG()} $key: $value")
    }
}

val Bundle.string
    get() = with(StringBuilder()) {
        append("{")
        for (key in keySet()) {
            val value = get(key)
            append(key).append(":").append(value)
        }
        append("}")
    }.toString()


/**
 * Calls specified [block], passing in a [Bundle] instance, which is returned
 * when the block completes.
 */
inline fun newBundle(block: Bundle.() -> Unit): Bundle = Bundle().apply {
    block(this)
}

/**
 * Shorthand for [Bundle.putBoolean].
 */
fun Bundle.put(key: String?, value: Boolean) {
    putBoolean(key, value)
}

/**
 * Shorthand for [Bundle.putByte].
 */
fun Bundle.put(key: String?, value: Byte) {
    putByte(key, value)
}

/**
 * Shorthand for [Bundle.putChar].
 */
fun Bundle.put(key: String?, value: Char) {
    putChar(key, value)
}

/**
 * Shorthand for [Bundle.putShort].
 */
fun Bundle.put(key: String?, value: Short) {
    putShort(key, value)
}

/**
 * Shorthand for [Bundle.putInt].
 */
fun Bundle.put(key: String?, value: Int) {
    putInt(key, value)
}

/**
 * Shorthand for [Bundle.putLong].
 */
fun Bundle.put(key: String?, value: Long) {
    putLong(key, value)
}

/**
 * Shorthand for [Bundle.putFloat].
 */
fun Bundle.put(key: String?, value: Float) {
    putFloat(key, value)
}

/**
 * Shorthand for [Bundle.putDouble].
 */
fun Bundle.put(key: String?, value: Double) {
    putDouble(key, value)
}

/**
 * Shorthand for [Bundle.putParcelable].
 */
fun Bundle.put(key: String?, value: Parcelable?) {
    putParcelable(key, value)
}

/**
 * Shorthand for [Bundle.putByteArray].
 */
fun Bundle.put(key: String?, value: ByteArray?) {
    putByteArray(key, value)
}

/**
 * Shorthand for [Bundle.putShortArray].
 */
fun Bundle.put(key: String?, value: ShortArray?) {
    putShortArray(key, value)
}

/**
 * Shorthand for [Bundle.putCharArray].
 */
fun Bundle.put(key: String?, value: CharArray?) {
    putCharArray(key, value)
}

/**
 * Shorthand for [Bundle.putFloatArray].
 */
fun Bundle.put(key: String?, value: FloatArray?) {
    putFloatArray(key, value)
}

/**
 * Shorthand for [Bundle.putIntArray].
 */
fun Bundle.put(key: String?, value: IntArray?) {
    putIntArray(key, value)
}

/**
 * Shorthand for [Bundle.putLongArray].
 */
fun Bundle.put(key: String?, value: LongArray?) {
    putLongArray(key, value)
}

/**
 * Shorthand for [Bundle.putString].
 */
fun Bundle.put(key: String?, value: String?) {
    putString(key, value)
}

/**
 * Shorthand for [Bundle.putBundle].
 */
fun Bundle.put(key: String?, value: Bundle?) {
    putBundle(key, value)
}

/**
 * Shorthand for [Bundle.putCharSequenceArray].
 */
fun Bundle.put(key: String?, value: Array<CharSequence?>?) {
    putCharSequenceArray(key, value)
}

/**
 * Shorthand for [Bundle.putParcelableArray].
 */
fun Bundle.put(key: String?, value: Array<Parcelable?>?) {
    putParcelableArray(key, value)
}

/**
 * Shorthand for [Bundle.putParcelableArrayList].
 */
fun Bundle.put(key: String?, value: ArrayList<out Parcelable?>?) {
    putParcelableArrayList(key, value)
}

/**
 * Shorthand for [Bundle.putSparseParcelableArray].
 */
fun Bundle.put(key: String?, value: SparseArray<out Parcelable?>?) {
    putSparseParcelableArray(key, value)
}

/**
 * Delegates a property to the arguments Bundle, keyed by the property name
 */
inline fun <reified T> Fragment.args(): ReadWriteProperty<Fragment, T> = bundleDelegate {
    if (arguments == null) arguments = Bundle()
    requireArguments()
}

inline fun <F, reified T> bundleDelegate(crossinline bundleProvider: (F) -> Bundle): ReadWriteProperty<F, T> = object : ReadWriteProperty<F, T> {
    override operator fun getValue(thisRef: F, property: KProperty<*>): T =
            bundleProvider(thisRef).get(property.name) as T

    override fun setValue(thisRef: F, property: KProperty<*>, value: T) =
            bundleProvider(thisRef).putAll(bundleOf(property.name to value))
}