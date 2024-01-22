package dev.funkymuse.customviews.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt


/**
 * Credit: [https://gist.github.com/Trikke/90efd4432fc09aaadf3e](https://gist.github.com/Trikke/90efd4432fc09aaadf3e)
 */
class StringMakeup(input: String) {


    private val sb: Spannable

    private val length: Int
        get() = sb.length

    init {
        sb = SpannableString(input)
    }


    fun strikethrough(start: Int, length: Int): StringMakeup {
        val span = StrikethroughSpan()
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun strikethrough(): StringMakeup {
        val span = StrikethroughSpan()
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun underline(start: Int, length: Int): StringMakeup {
        val span = UnderlineSpan()
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun underline(): StringMakeup {
        val span = UnderlineSpan()
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun boldify(start: Int, length: Int): StringMakeup {
        val span = StyleSpan(Typeface.BOLD)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun boldify(): StringMakeup {
        val span = StyleSpan(Typeface.BOLD)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun italize(start: Int, length: Int): StringMakeup {
        val span = StyleSpan(Typeface.ITALIC)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun italize(): StringMakeup {
        val span = StyleSpan(Typeface.ITALIC)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun colorize(start: Int, length: Int, @ColorInt color: Int): StringMakeup {
        val span = ForegroundColorSpan(color)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun colorize(@ColorInt color: Int): StringMakeup {
        val span = ForegroundColorSpan(color)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun mark(start: Int, length: Int, @ColorInt color: Int): StringMakeup {
        val span = BackgroundColorSpan(color)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun mark(color: Int): StringMakeup {
        val span = BackgroundColorSpan(color)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun proportionate(start: Int, length: Int, proportion: Float): StringMakeup {
        val span = RelativeSizeSpan(proportion)
        sb.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun proportionate(proportion: Float): StringMakeup {
        val span = RelativeSizeSpan(proportion)
        sb.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    fun apply(): Spannable {
        return sb
    }
}