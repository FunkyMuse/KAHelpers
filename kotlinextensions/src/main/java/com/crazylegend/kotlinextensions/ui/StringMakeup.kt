package com.crazylegend.kotlinextensions.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.*
import androidx.annotation.ColorInt
import androidx.annotation.NonNull


/**
 * Credit: [https://gist.github.com/Trikke/90efd4432fc09aaadf3e](https://gist.github.com/Trikke/90efd4432fc09aaadf3e)
 */
class StringMakeup(@NonNull input: String) {

    @NonNull
    private val sb: Spannable

    private val length: Int
        get() = sb.length

    init {
        sb = SpannableString(input)
    }

    @NonNull
    fun strikethrough(start: Int, length: Int): StringMakeup {
        val span = StrikethroughSpan()
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun strikethrough(): StringMakeup {
        val span = StrikethroughSpan()
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun underline(start: Int, length: Int): StringMakeup {
        val span = UnderlineSpan()
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun underline(): StringMakeup {
        val span = UnderlineSpan()
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun boldify(start: Int, length: Int): StringMakeup {
        val span = StyleSpan(Typeface.BOLD)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun boldify(): StringMakeup {
        val span = StyleSpan(Typeface.BOLD)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun italize(start: Int, length: Int): StringMakeup {
        val span = StyleSpan(Typeface.ITALIC)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun italize(): StringMakeup {
        val span = StyleSpan(Typeface.ITALIC)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun colorize(start: Int, length: Int, @ColorInt color: Int): StringMakeup {
        val span = ForegroundColorSpan(color)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun colorize(@ColorInt color: Int): StringMakeup {
        val span = ForegroundColorSpan(color)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun mark(start: Int, length: Int, @ColorInt color: Int): StringMakeup {
        val span = BackgroundColorSpan(color)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun mark(color: Int): StringMakeup {
        val span = BackgroundColorSpan(color)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun proportionate(start: Int, length: Int, proportion: Float): StringMakeup {
        val span = RelativeSizeSpan(proportion)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun proportionate(proportion: Float): StringMakeup {
        val span = RelativeSizeSpan(proportion)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    @NonNull
    fun apply(): Spannable {
        return sb
    }
}