package com.funkymuse.kotlinextensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt
import java.util.*
import java.util.regex.Pattern




private val FORMAT_SEQUENCE = Pattern.compile("%([0-9]+\\$|<?)([^a-zA-z%]*)([[a-zA-Z%]&&[^tT]]|[tT][a-zA-Z])")
private const val CONCATENATE_FORMATTER = "%1\$s%2\$s"

fun CharSequence.appendNewLine() = CONCATENATE_FORMATTER.formatSpanned(this, "\n")

fun CharSequence.bold() = applyTags(arrayOf(this), StyleSpan(Typeface.BOLD))

fun CharSequence.italic() = applyTags(arrayOf(this), StyleSpan(Typeface.ITALIC))

fun CharSequence.underline() = applyTags(arrayOf(this), UnderlineSpan())

fun CharSequence.scale(relativeSize: Float) = applyTags(arrayOf(this), RelativeSizeSpan(relativeSize))

fun CharSequence.backgroundColor(@ColorInt color: Int) = applyTags(arrayOf(this), BackgroundColorSpan(color))

fun CharSequence.strikeThrough() = applyTags(arrayOf(this), StrikethroughSpan())

fun CharSequence.superScript() = applyTags(arrayOf(this), SuperscriptSpan())

fun CharSequence.subScript() = applyTags(arrayOf(this), SubscriptSpan())

fun CharSequence.color(@ColorInt color: Int) = applyTags(arrayOf(this), ForegroundColorSpan(color))

fun CharSequence.click(paintConsumer: (TextPaint) -> Unit = {}, clickAction: () -> Unit) = applyTags(arrayOf(this), object : ClickableSpan() {
    override fun onClick(widget: View) = clickAction.invoke()

    override fun updateDrawState(paint: TextPaint) = paintConsumer.invoke(paint)
})

/**
 * Version of [String.format] that works on [Spanned] strings to preserve rich text formatting.
 * Both the `format` as well as any `%s args` can be Spanned and will have their formatting preserved.
 * Due to the way [Spannable]s work, any argument's spans will can only be included **once** in the result.
 * Any duplicates will appear as text only.
 *
 * @param this@formatSpanned the format string (see [java.util.Formatter.format])
 * @param args   the list of arguments passed to the formatter. If there are
 * more arguments than required by `format`,
 * additional arguments are ignored.
 * @return the formatted string (with spans).
 */
fun CharSequence.formatSpanned(vararg args: Any): SpannableStringBuilder =
        formatActual(Locale.getDefault(), this, *args)

/**
 * Returns a CharSequence that concatenates the specified array of CharSequence
 * objects and then applies a list of zero or more tags to the entire range.
 *
 * @param content an array of character sequences to apply a style to
 * @param tags    the styled span objects to apply to the content
 * such as android.text.style.StyleSpan
 */
private fun applyTags(content: Array<out CharSequence>, vararg tags: Any): CharSequence {
    val text = SpannableStringBuilder()
    openTags(text, tags)

    for (item in content) text.append(item)

    closeTags(text, tags)
    return text
}

/**
 * Iterates over an array of tags and applies them to the beginning of the specified
 * Spannable object so that future text appended to the text will have the styling
 * applied to it. Do not call this method directly.
 */
private fun openTags(text: Spannable, tags: Array<out Any>) {
    for (tag in tags) text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK)
}

/**
 * "Closes" the specified tags on a Spannable by updating the spans to be
 * endpoint-exclusive so that future text appended to the end will not take
 * on the same styling. Do not call this method directly.
 */
private fun closeTags(text: Spannable, tags: Array<out Any>) {
    val len = text.length
    for (tag in tags)
        if (len > 0) text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        else text.removeSpan(tag)
}

/**
 * Version of [String.formatSpanned] that works on [Spanned] strings to preserve rich text formatting.
 * Both the `format` as well as any `%s args` can be Spanned and will have their formatting preserved.
 * Due to the way [Spannable]s work, any argument's spans will can only be included **once** in the result.
 * Any duplicates will appear as text only.
 *
 * @param locale the locale to apply; `null` value means no localization.
 * @param format the format string (see [java.util.Formatter.format])
 * @param args   the list of arguments passed to the formatter.
 * @return the formatted string (with spans).
 * @see String.formatSpanned
 */
private fun formatActual(locale: Locale, format: CharSequence, vararg args: Any): SpannableStringBuilder {
    val out = SpannableStringBuilder(format)

    var i = 0
    var argAt = -1

    while (i < out.length) {
        val m = FORMAT_SEQUENCE.matcher(out)
        if (!m.find(i)) break
        i = m.start()
        val exprEnd = m.end()

        val argTerm = m.group(1)
        val modTerm = m.group(2)
        val typeTerm = m.group(3)

        val cookedArg: CharSequence

        when (typeTerm) {
            "%" -> cookedArg = "%"
            "n" -> cookedArg = "\n"
            else -> {
                val argIdx: Int = when (argTerm) {
                    "" -> ++argAt
                    "<" -> argAt
                    else -> Integer.parseInt(argTerm!!.substring(0, argTerm.length - 1)) - 1
                }

                val argItem = args[argIdx]

                cookedArg =
                        if (typeTerm == "s" && argItem is Spanned) argItem
                        else String.format(locale, "%$modTerm$typeTerm", argItem)
            }
        }

        out.replace(i, exprEnd, cookedArg)
        i += cookedArg.length
    }

    return out
}