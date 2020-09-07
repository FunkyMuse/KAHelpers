package com.crazylegend.kotlinextensions.string

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.*
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.crazylegend.kotlinextensions.base64Encode
import com.crazylegend.kotlinextensions.base64EncodeToString
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern


inline fun String.ifIsNullOrEmpty(action: () -> Unit) {
    if (isNullOrEmpty()) action()
}

inline fun String.ifIsNotNullOrEmpty(action: () -> Unit) {
    if (!isNullOrEmpty()) action()
}

fun String.urlEncoded(): String? = URLEncoder.encode(this, "utf-8")


inline val String?.doubleValue: Double
    get() = if (TextUtils.isEmpty(this)) 0.0 else try {
        this!!.toDouble()
    } catch (e: Exception) {
        0.0
    }

inline val String?.intValue: Int
    get() = if (TextUtils.isEmpty(this)) 0 else try {
        this!!.toInt()
    } catch (e: Exception) {
        0
    }

inline val String?.floatValue: Float
    get() = if (TextUtils.isEmpty(this)) 0f else try {
        this!!.toFloat()
    } catch (e: Exception) {
        0f
    }

inline val CharSequence?.intValue: Int
    get() = toString().intValue

inline val CharSequence?.floatValue: Float
    get() = toString().floatValue


inline val String.isIp: Boolean
    get() {
        val p = Pattern.compile(
                "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
        val m = p.matcher(this)
        return m.find()
    }

fun stringPairOf(vararg pair: Pair<String, Any?>): String {
    val strList = ArrayList<String>(pair.size)
    for ((key, value) in pair) {
        strList.add("$key: $value")
    }
    return strList.joinToString()
}


const val NEW_LINE = "\n"


fun String.wrapInQuotes(): String {
    var formattedConfigString: String = this
    if (!startsWith("\"")) {
        formattedConfigString = "\"$formattedConfigString"
    }
    if (!endsWith("\"")) {
        formattedConfigString = "$formattedConfigString\""
    }
    return formattedConfigString
}

fun String.unwrapQuotes(): String {
    var formattedConfigString: String = this
    if (formattedConfigString.startsWith("\"")) {
        if (formattedConfigString.length > 1) {
            formattedConfigString = formattedConfigString.substring(1)
        } else {
            formattedConfigString = ""
        }
    }
    if (formattedConfigString.endsWith("\"")) {
        if (formattedConfigString.length > 1) {
            formattedConfigString =
                    formattedConfigString.substring(0, formattedConfigString.length - 1)
        } else {
            formattedConfigString = ""
        }
    }
    return formattedConfigString
}


fun CharSequence.isEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.orDefault(defaultValue: CharSequence): CharSequence = if (isNullOrBlank()) defaultValue else this!!


fun String?.urlEncode(charsetName: String = "UTF-8"): String {
    if (this.isNullOrEmpty()) return ""
    try {
        return URLEncoder.encode(this, charsetName)
    } catch (e: UnsupportedEncodingException) {
        throw AssertionError(e)
    }
}

fun String?.urlDecode(charsetName: String = "UTF-8"): String {
    if (this.isNullOrEmpty()) return ""
    try {
        return URLDecoder.decode(this, charsetName)
    } catch (e: UnsupportedEncodingException) {
        throw AssertionError(e)
    }
}


fun charToByte(c: Char): Byte {
    return "0123456789ABCDEF".indexOf(c).toByte()
}

fun String.convertToBytes(): ByteArray {
    if (this == "") {
        return ByteArray(0)
    }
    val newHexString = this.trim().toUpperCase()
    val length = newHexString.length / 2
    val hexChars = newHexString.toCharArray()
    val d = ByteArray(length)
    for (i in 0 until length) {
        val pos = i * 2
        d[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
    }
    return d
}


fun String.base64Encode(): ByteArray {
    return this.toByteArray().base64Encode()
}

fun String.base64EncodeToString(): String {
    return this.toByteArray().base64EncodeToString()
}

fun String.base64Decode(): ByteArray {
    if (this.isEmpty()) return ByteArray(0)
    return Base64.decode(this, Base64.NO_WRAP)
}

fun CharSequence?.isMatch(regex: String): Boolean {
    return !this.isNullOrEmpty() && Regex(regex).matches(this)
}

fun CharSequence.setBackgroundColor(color: Int): CharSequence {
    val s = SpannableString(this)
    s.setSpan(BackgroundColorSpan(color), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return s
}

fun CharSequence.setForegroundColor(color: Int): CharSequence {
    val s = SpannableString(this)
    s.setSpan(ForegroundColorSpan(color), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return s
}

fun String.safeBoolean(default: Boolean = false) = try {
    toBoolean()
} catch (e: Exception) {
    default
}

fun String.safeByte(default: Byte = 0) = toByteOrNull().safe(default)
fun String.safeShort(default: Short = 0) = toShortOrNull().safe(default)
fun String.safeInt(default: Int = 0) = toIntOrNull().safe(default)
fun String.safeLong(default: Long = 0L) = toLongOrNull().safe(default)
fun String.safeFloat(default: Float = 0f) = toFloatOrNull().safe(default)
fun String.safeDouble(default: Double = 0.0) = toDoubleOrNull().safe(default)


inline fun <reified T> T?.safe(default: T) = this ?: default


fun String.ifBlank(mapper: () -> String): String =
        if (isBlank()) mapper() else this

fun String.ifEmpty(mapper: () -> String): String =
        if (isEmpty()) mapper() else this

fun String?.ifNull(mapper: () -> String): String =
        this ?: mapper()


/**
 * Take some text, highlight some text with a color and add a click listener to it
 * @param source Text to click
 * @param color Color to change too, default = null,
 * @param onClick Callback
 */
fun SpannableString.onClick(source: String, shouldUnderline: Boolean = true, shouldBold: Boolean = true, color: Int? = null, textView: TextView? = null, onClick: () -> Unit): SpannableString {
    val startIndex = this.toString().indexOf(source)
    if (startIndex == -1) {
        throw Exception("Cannot highlight this title as $source is not contained with $this")
    }
    this.setSpan(object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            if (color != null) {
                ds.color = color
                ds.bgColor = Color.TRANSPARENT
            }
        }

        override fun onClick(widget: View) {
            onClick()
            textView?.clearFocus()
            textView?.invalidate()
        }
    }, startIndex, startIndex + source.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (shouldUnderline) {
        this.setSpan(UnderlineSpan(), startIndex, startIndex + source.length, 0)
    }
    if (shouldBold) {
        this.setSpan(StyleSpan(Typeface.BOLD), startIndex, startIndex + source.length, 0)
    }
    return this
}

/**
 * Highlight a given word in a string with a given colour
 */
fun String.highlight(source: String, color: Int): SpannableString {
    val startIndex = this.indexOf(source)
    if (startIndex == -1) {
        throw Exception("Cannot highlight this title as $source is not contained with $this")
    }
    val spannable = SpannableString(this)
    spannable.setSpan(object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
        }

        override fun onClick(widget: View) {}
    }, startIndex, startIndex + source.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}


/**
 * If the string is a HTTP URL (ie. Starts with http:// or https://)
 */
fun String.isHttp(): Boolean {
    return this.matches(Regex("(http|https)://[^\\s]*"))
}

/**
 * Convert any string value into it's enum value by a given property of the enum
 */
inline fun <reified T : Enum<T>> String.toEnum(by: (enum: T) -> String = { it.name }): T? {
    return enumValues<T>().firstOrNull { by(it) == this }
}
