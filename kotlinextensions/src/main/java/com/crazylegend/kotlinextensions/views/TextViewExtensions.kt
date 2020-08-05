package com.crazylegend.kotlinextensions.views

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.context.getFontCompat
import com.google.android.material.textfield.TextInputLayout


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
 * Removes the bolding of a text view
 */
fun TextView.unBold() {
    setTypeface(null, Typeface.NORMAL)
}

fun TextView.normal() {
    setTypeface(null, Typeface.NORMAL)
}


fun TextView.setAppereance(resId: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    setTextAppearance(resId)
} else {
    @Suppress("DEPRECATION") setTextAppearance(context, resId)
}


/**
 * Bold the TextView.
 */
fun TextView.bold() {
    setTypeface(null, Typeface.BOLD)
}

fun TextView.boldItalic() {
    setTypeface(null, Typeface.BOLD_ITALIC)
}


fun TextView.italic() {
    setTypeface(null, Typeface.ITALIC)
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


fun TextView.preComputeCurrentText() {
    val textParams = TextViewCompat.getTextMetricsParams(this)
    val text = PrecomputedTextCompat.create(text, textParams)
    this.text = text
}

@WorkerThread
fun TextView.precomputeSpannableText(text: Spannable): PrecomputedTextCompat {
    val textParams = TextViewCompat.getTextMetricsParams(this)
    return PrecomputedTextCompat.create(text, textParams)
}

@WorkerThread
fun TextView.precomputeText(text: String): PrecomputedTextCompat {
    val textParams = TextViewCompat.getTextMetricsParams(this)
    return PrecomputedTextCompat.create(text, textParams)
}


inline fun TextView.addTextChangedListener(
        crossinline onBeforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
        crossinline onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
        crossinline onAfterTextChanged: (s: Editable) -> Unit = { }
): TextWatcher {
    val listener = object : TextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                onTextChanged(s, start, before, count)

        override fun afterTextChanged(s: Editable) = onAfterTextChanged(s)
        override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
        ) = onBeforeTextChanged(s, start, count, after)


    }

    addTextChangedListener(listener)
    return listener
}

infix fun TextView.set(@StringRes id: Int) {
    setText(id)
}

infix fun TextView.set(text: String?) {
    setText(text)
}

infix fun TextView.set(text: Spannable?) {
    setText(text)
}


infix fun AppCompatTextView.set(text: String?) {
    setPrecomputedText(text)
}


fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}

val TextView.textString: String
    get() = text.toString()


val AppCompatTextView.textString: String
    get() = text.toString()


fun TextView.setTextColorId(id: Int) {
    this.setTextColor(this.context.getColorCompat(id))
}

fun TextView.setRightDrawable(@DrawableRes resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
}

fun TextView.setFont(@FontRes font: Int) {
    this.typeface = context.getFontCompat(font)
}

fun TextView.setFont(typeface: Typeface?) {
    this.typeface = typeface
}


fun TextView.addTextListener(
        beforeChanged: ((s: CharSequence) -> Unit)? = null,
        onChanged: ((s: CharSequence) -> Unit)? = null,
        afterChanged: ((s: Editable) -> Unit)? = null
) {
    addDebounceTextListener(
            debounceTimeInMillis = 0,
            beforeChanged = beforeChanged, onChanged = onChanged, afterChanged = afterChanged
    )
}

fun TextView.addDebounceTextListener(
        debounceTimeInMillis: Long = 500,
        beforeChanged: ((s: CharSequence) -> Unit)? = null,
        afterChanged: ((s: Editable) -> Unit)? = null,
        onChanged: ((s: CharSequence) -> Unit)? = null
) {
    addTextChangedListener(object : TextWatcher {
        private val changedRunnable: Runnable = Runnable {
            onChanged?.invoke(text)
        }

        override fun afterTextChanged(s: Editable) {
            afterChanged?.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            beforeChanged?.invoke(s)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (onChanged == null) return

            if (debounceTimeInMillis <= 0) {
                onChanged.invoke(s)
                return
            }

            removeCallbacks(changedRunnable)
            postDelayed(changedRunnable, debounceTimeInMillis)
        }
    })
}

fun TextView.addDebounceChangeStateListener(delayInMillis: Long = 500, timeoutInMillis: Long = 0, listener: (Boolean) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        private var start: Boolean = false
        private val runnable: Runnable = Runnable {
            start = false
            listener(false)
            removeCallbacks(timeoutRunnable)

        }

        private val timeoutRunnable: Runnable = object : Runnable {
            override fun run() {
                listener(true)
                postDelayed(this, timeoutInMillis)
            }
        }

        override fun afterTextChanged(s: Editable) {
            //nothing
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!this.start) {
                this.start = true
                listener(true)
                postDelayed(timeoutRunnable, timeoutInMillis)
            } else {
                if (s.isEmpty()) {
                    listener(false)
                    this.start = false
                    removeCallbacks(timeoutRunnable)
                }
            }
            removeCallbacks(runnable)
            postDelayed(runnable, delayInMillis)
        }
    })
}


fun AppCompatTextView.setPrecomputedText(text: String?) {
    text?.let {
        setTextFuture(PrecomputedTextCompat.getTextFuture(it, this.textMetricsParamsCompat, null))
    }
}

fun AppCompatTextView.setPrecomputedTextOrHide(text: String?) {
    if (text == null) {
        gone()
    } else {
        visible()
        setPrecomputedText(text)
    }
}

fun TextView.setTextStrikeThru(strikeThru: Boolean) {
    if (strikeThru) setTextStrikeThru() else setTextNotStrikeThru()
}

fun TextView.setTextStrikeThru() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.setTextNotStrikeThru() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun TextView.setTextUnderlined(underlined: Boolean) {
    if (underlined) setTextUnderlined() else setTextNotUnderlined()
}

fun TextView.setTextUnderlined() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.setTextNotUnderlined() {
    paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}


fun TextView.setDrawableLeft(drawable: Drawable?, width: Int = 0, height: Int = width) =
        setDrawables(drawable, width = width, height = height)

fun TextView.setDrawables(
        left: Drawable? = null,
        top: Drawable? = null,
        right: Drawable? = null,
        bottom: Drawable? = null,
        width: Int = 0,
        height: Int = 0
) =
        if (width == 0 || height == 0)
            setDrawablesWithIntrinsicBounds(left, top, right, bottom)
        else
            setCompoundDrawables(
                    left?.apply { setSize(width, height) },
                    top?.apply { setSize(width, height) },
                    right?.apply { setSize(width, height) },
                    bottom?.apply { setSize(width, height) }
            )

fun TextView.setDrawablesWithIntrinsicBounds(
        left: Drawable? = null,
        top: Drawable? = null,
        right: Drawable? = null,
        bottom: Drawable? = null
) =
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)


fun TextView.ellipsizeDynamic(text: String) {
    this.text = text
    this.afterLatestMeasured {
        val noOfLinesVisible = this.height / (this.lineHeight)
        this.maxLines = noOfLinesVisible
        this.ellipsize = TextUtils.TruncateAt.END
    }
}

fun TextView.ellipsizeViewPager(text: String) {
    this.text = text
    this.afterLatestMeasured {
        val noOfLinesVisible = this.height / (this.lineHeight.toDouble() * 0.84).toInt()
        this.maxLines = noOfLinesVisible
        this.ellipsize = TextUtils.TruncateAt.END
    }
}

fun TextView.setTextOrHide(text: String?) {
    text?.let {
        this.text = it
    } ?: run {
        this.visibility = View.GONE
    }
}

fun AppCompatTextView.setTextOrHide(text: String?) {
    text?.let {
        this.text = it
    } ?: run {
        this.visibility = View.GONE
    }
}

var TextView.compoundDrawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(value,
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3])

var TextView.compoundDrawableTop: Drawable?
    get() = compoundDrawablesRelative[1]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            value,
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3])

var TextView.compoundDrawableEnd: Drawable?
    get() = compoundDrawablesRelative[2]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            compoundDrawablesRelative[1],
            value,
            compoundDrawablesRelative[3])

var TextView.compoundDrawableBottom: Drawable?
    get() = compoundDrawablesRelative[3]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            value)


/**
 * Measuring TextView width. Use this extension after view rendered
 */
fun TextView.measureWidth(): Int {
    val bound = Rect()
    this.paint.getTextBounds(this.text.toString(), 0, this.text.toString().length, bound)
    return bound.width()
}

/**
 * Measuring TextView height. Use this extension after view rendered
 */
fun TextView.measureHeight(): Int {
    val bound = Rect()
    this.paint.getTextBounds(this.text.toString(), 0, this.text.toString().length, bound)
    return bound.height()
}


/**
 * Setting new text to TextView with something like fade to alpha animation
 *
 * @property text - text to set to TextView
 * @property duration - animation final duration
 */
fun TextView.setTextWithAnimation(text: String, duration: Long) {
    val stepDuration = duration / 2
    this.animate()
            .alpha(0f)
            .setDuration(stepDuration)
            .withEndAction {
                this.text = text
                this.animate()
                        .alpha(1f)
                        .setDuration(stepDuration)
                        .start()
            }.start()
}

/**
 * Setting new text to TextView with something like width transition animation
 *
 * @property text - text to set to TextView
 * @property animDuration - animation final duration
 */
fun TextView.setTextWithTransition(text: String, animDuration: Long) {
    val with = this.width
    val thisText = this
    val textLayoutParams = this.layoutParams
    ValueAnimator.ofInt(with, 0).apply {
        addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = textLayoutParams
            layoutParams.width = value
            thisText.layoutParams = layoutParams
        }
        doOnEnd {
            thisText.text = text
            thisText.measure(0, 0)
            ValueAnimator.ofInt(0, thisText.measuredWidth).apply {
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val layoutParams: ViewGroup.LayoutParams = textLayoutParams
                    layoutParams.width = value
                    thisText.layoutParams = layoutParams
                }
                duration = animDuration
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        }
        duration = animDuration
        interpolator = AccelerateDecelerateInterpolator()
    }.start()
}


fun AppCompatTextView.setTextAndShowOrGone(textString: String?) {
    text = if (textString.isNullOrEmpty()) {
        gone()
        null
    } else {
        visible()
        textString
    }
}

fun AppCompatTextView.setTextAndShowOrGone(context: Context, stringRes: Int, vararg params: Any? = emptyArray()) {
    val string = context.getString(stringRes, params)
    text = if (params.isNullOrEmpty()) {
        gone()
        null
    } else {
        visible()
        string
    }
}

fun AppCompatTextView.setTextAndShowOrInvisible(context: Context, stringRes: Int, vararg params: Any? = emptyArray()) {
    val string = context.getString(stringRes, params)
    text = if (params.isNullOrEmpty()) {
        invisible()
        null
    } else {
        visible()
        string
    }
}

fun AppCompatTextView.setTextAndShowOrInvisible(textString: String?) {
    text = if (textString.isNullOrEmpty()) {
        invisible()
        null
    } else {
        visible()
        textString
    }
}
