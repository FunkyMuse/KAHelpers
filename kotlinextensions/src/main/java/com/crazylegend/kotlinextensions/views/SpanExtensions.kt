package com.crazylegend.kotlinextensions.views

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.*
import android.view.View


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

/**
 * Change the foreground color of the text specified in a paragraph of text
 * @param range The range of text to change the foreground color
 * @param color The color to change, the default is red
 */
fun CharSequence.toColorSpan(range: IntRange = IntRange(0, length), color: Int = Color.RED): SpannableString {
    return SpannableString(this).apply {
        setSpan(ForegroundColorSpan(color), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Change the background color of the text specified in a paragraph of text
 * @param range The range of text to change the background color
 * @param color The color to change, the default is red
 */
fun CharSequence.toBackgroundColorSpan(range: IntRange = IntRange(0, length), color: Int = Color.RED): SpannableString {
    return SpannableString(this).apply {
        setSpan(BackgroundColorSpan(color), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Change the text to bold
 * @param range The range of text to change the bold
 */
fun CharSequence.toBoldSpan(range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StyleSpan(Typeface.BOLD), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}


/**
 * Change the text to italic
 * @param range The range of text to change the italic
 */
fun CharSequence.toItalicSpan(range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StyleSpan(Typeface.ITALIC), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}


/**
 * Change the text to bold italic
 * @param range The range of text to change the bold italic
 */
fun CharSequence.toBoldItalicSpan(range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StyleSpan(Typeface.BOLD_ITALIC), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}


/**
 * Change the text to normal text
 * @param range The range of text to change the normal style
 */
fun CharSequence.toNormalSpan(range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StyleSpan(Typeface.NORMAL), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Set the span to a styled one from Typeface
 * @param style one of [Typeface.NORMAL] etc..
 */
fun CharSequence.toStyleSpan(style: Int, range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StyleSpan(style), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}


/**
 * Add a strike through to the text of the specified range in the text
 * @param range The range of text to add strike through to
 */
fun CharSequence.toStrikeThroughSpan(range: IntRange = IntRange(0, length)): SpannableString {
    return SpannableString(this).apply {
        setSpan(StrikethroughSpan(), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Add color and click event to the text of the specified range in the text
 * @param range Range of target text
 */
fun CharSequence.toClickSpan(range: IntRange = IntRange(0, length), color: Int = Color.RED, isUnderlineText: Boolean = false, clickListener: View.OnClickListener): SpannableString {
    return SpannableString(this).apply {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickListener.onClick(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = isUnderlineText
            }
        }
        setSpan(clickableSpan, range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Change the size of the text in the specified range of text
 * @param range The range of text to be resized
 * @param scale The zoom value, greater than 1, is larger than other text; less than 1, it is smaller than other text; the default is 1.5
 */
fun CharSequence.toSizeSpan(range: IntRange = IntRange(0, length), scale: Float = 1.5f): SpannableString {
    return SpannableString(this).apply {
        setSpan(RelativeSizeSpan(scale), range.first, range.last, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}


