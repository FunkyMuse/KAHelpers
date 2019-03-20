package com.crazylegend.kotlinextensions.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.crazylegend.kotlinextensions.context.clipboardManager


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

/**
 * UnderLine the TextView.
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * DeleteLine for a TextView.
 */
fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}


/**
 * Bold the TextView.
 */
fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}


/**
 * Set font for TextView.
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}


/**
 * Set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        Log.d("ViewExtensions", "exception in setColorOfSubstring, text=$text, substring=$substring", e)
    }
}

/**
 * Set a drawable to the left of a TextView.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * Set a drawable to the top of a TextView.
 */
fun TextView.setDrawableTop(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}


/**
 * Set a drawable to the right of a TextView.
 */
fun TextView.setDrawableRight(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

/**
 * Set a drawable to the bottom of a TextView.
 */
fun TextView.setDrawableBottom(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
}


fun TextView.sizeSpan(str: String, range: IntRange, scale: Float = 1.5f) {
    text = str.toSizeSpan(range, scale)
}


fun TextView.colorSpan(str: String, range: IntRange, color: Int = Color.RED) {
    text = str.toColorSpan(range, color)
}


fun TextView.backgroundColorSpan(str: String, range: IntRange, color: Int = Color.RED) {
    text = str.toBackgroundColorSpan(range, color)
}

fun TextView.strikeThrougthSpan(str: String, range: IntRange) {
    text = str.toStrikeThroughSpan(range)
}

fun TextView.clickSpan(
    str: String, range: IntRange,
    color: Int = Color.RED, isUnderlineText: Boolean = false, clickListener: View.OnClickListener
) {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT  // remove click bg color
    text = str.toClickSpan(range, color, isUnderlineText, clickListener)
}


/**
 * Copies TextView text to clipboard with given label
 */
fun TextView.copyToClipboard(label: String) {
    if (text != null) {
        val manager= context.clipboardManager
        manager?.primaryClip = ClipData.newPlainText(label, text)
    }
}


/**
 * Copies TextView text to clipboard with given label
 */
fun TextView.copyToClipboard() {
    if (text != null) {
        val manager= context.clipboardManager
        manager?.primaryClip = ClipData.newPlainText("", text)
    }
}

/**
 * Set TextView from Html
 */
fun TextView.setTextFromHtml(html: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        this.text = Html.fromHtml(html)
    }
}

/**
 * Sets given content to TextView or hides it.
 */
fun TextView.setAsContent(content: CharSequence?) {
    if (!TextUtils.isEmpty(content)) {
        text = content
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

inline var TextView.isSelectable: Boolean
    get() = isTextSelectable
    set(value) = setTextIsSelectable(value)


fun TextView.updateTextAppearance(@StyleRes resource: Int) =
    TextViewCompat.setTextAppearance(this, resource)

@SuppressLint("RestrictedApi")
fun TextView.textColorAnim(from: Int, to: Int) {
    val textColorAnimator = ObjectAnimator.ofObject(
        this,
        "textColor",
        ArgbEvaluator(),
        ContextCompat.getColor(context, from),
        ContextCompat.getColor(context, to)
    )
    textColorAnimator.duration = 300
    textColorAnimator.start()
}

@WorkerThread
fun TextView.precomputeText(text: Spannable): PrecomputedTextCompat {
    val textParams = TextViewCompat.getTextMetricsParams(this)
    return PrecomputedTextCompat.create(text, textParams)
}