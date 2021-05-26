package com.crazylegend.kotlinextensions.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout.*
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.UiThread
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import kotlin.math.roundToInt

/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun TextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

fun AppCompatTextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

fun View.px(@DimenRes rid: Int): Int {
    return this.context.resources.getDimensionPixelOffset(rid)
}


val SearchView?.getEditTextSearchView get() = this?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)


fun collapseLayout(linearLayout: LinearLayout, imageView: ImageView, dropUPIMG: Int, dropDOWNIMG: Int) {
    var firstClick = false

    imageView.setOnClickListener {
        if (firstClick) {

            TransitionManager.beginDelayedTransition(linearLayout)
            linearLayout.visibility = View.GONE
            imageView.setImageResource(dropDOWNIMG)

            firstClick = false

        } else {
            TransitionManager.beginDelayedTransition(linearLayout)
            linearLayout.visibility = View.VISIBLE
            imageView.setImageResource(dropUPIMG)

            firstClick = true

        }
    }


}


fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
    val alpha = (Color.alpha(color) * factor).roundToInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)
fun View.setPaddingTop(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)
fun View.setPaddingBottom(value: Int) = setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)
fun View.setPaddingStart(value: Int) = setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)
fun View.setPaddingEnd(value: Int) = setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)
fun View.setPaddingHorizontal(value: Int) = setPaddingRelative(value, paddingTop, value, paddingBottom)
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)

fun View.setHeight(newValue: Int) {
    val params = layoutParams
    params?.let {
        params.height = newValue
        layoutParams = params
    }
}

fun View.setWidth(newValue: Int) {
    val params = layoutParams
    params?.let {
        params.width = newValue
        layoutParams = params
    }
}


fun View.resize(width: Int, height: Int) {
    val params = layoutParams
    params?.let {
        params.width = width
        params.height = height
        layoutParams = params
    }
}

/**
 * INVISIBLE TO VISIBLE AND OTHERWISE
 */
fun View.toggleVisibilityInvisibleToVisible(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    return this
}

/**
 * INVISIBLE TO GONE AND OTHERWISE
 */
fun View.toggleVisibilityGoneToVisible(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}


/**
 *  View as bitmap.
 */
fun View.getBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    canvas.save()
    return bitmap
}

/**
 * Method to simplify the code needed to apply spans on a specific sub string.
 */
inline fun SpannableStringBuilder.withSpan(vararg spans: Any, action: SpannableStringBuilder.() -> Unit):
        SpannableStringBuilder {
    val from = length
    action()

    for (span in spans) {
        setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

@UiThread
fun View.fadeOut() {
    fadeOut(500)
}

@UiThread
fun View.fadeIn() {
    fadeIn(500)
}

@UiThread
fun View.fadeIn(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 1.0f)
    anim.duration = duration
    this.startAnimation(anim)
}

@UiThread
fun View.fadeOut(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 0.0f)
    anim.duration = duration
    this.startAnimation(anim)
}


/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 */
inline fun <T : View> T.afterMeasured(crossinline function: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function()
            }
        }
    })
}


val View.isVisibile: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }

val View.isGone: Boolean
    get() {
        return this.visibility == View.GONE
    }

val View.isInvisible: Boolean
    get() {
        return this.visibility == View.INVISIBLE
    }

/**
 * Sets color to status bar
 */
fun Window.addStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(this.context, color)
    }
}

/**
 * Visible if condition met
 */

inline fun View.visibleIf(block: () -> Boolean) {
    if (visibility != View.VISIBLE && block()) {
        visible()
    }
}


/**
 * Visible if condition met else gone
 */

inline fun View.visibleIfElseGone(block: () -> Boolean) {
    if (visibility != View.VISIBLE && block()) {
        visible()
    } else {
        gone()
    }
}


/**
 * Invisible if condition met
 */

inline fun View.invisibleIf(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        invisible()
    }
}


/**
 * Invisible if condition met
 */

inline fun View.invisibleIfElseVisible(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        invisible()
    } else {
        visible()
    }
}


/**
 * Gone if condition met
 */
inline fun View.goneIf(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        gone()
    }
}


/**
 * Gone if condition met
 */
inline fun View.goneIfElseVisible(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        gone()
    } else {
        visible()
    }
}


/**
 * Aligns to left of the parent in relative layout
 */
fun View.alignParentStart() {
    val params = layoutParams as LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_START)
    }

}


/**
 * Aligns to right of the parent in relative layout
 */
fun View.alignParentEnd() {
    val params = layoutParams as LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_END)
    }

}


/**
 * Aligns in the center of the parent in relative layout
 */
fun View.alignInCenter() {
    val params = layoutParams as LayoutParams?

    params?.apply {
        addRule(CENTER_HORIZONTAL)
    }

}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(left, top, right, bottom)
    }

    this.layoutParams = params

}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(size: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(size)
    }
    this.layoutParams = params

}


/**
 * Sets right margin for views in Linear Layout
 */
fun View.endLinearMargin(size: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        marginEnd = size
    }
    this.layoutParams = params

}


/**
 * Sets bottom margin for views in Linear Layout
 */
fun View.bottomLinearMargin(size: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop, marginRight, size)
    }
    this.layoutParams = params

}

/**
 * Sets top margin for views in Linear Layout
 */
fun View.topLinearMargin(size: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }
    this.layoutParams = params

}


/**
 * Sets top margin for views in Linear Layout
 */
fun View.startLinearMargin(size: Int) {
    val params = layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        marginStart = size
    }
    this.layoutParams = params

}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        setMargins(left, top, right, bottom)
    }
    this.layoutParams = params

}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(size: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        setMargins(size)
    }
    this.layoutParams = params

}


/**
 * Sets right margin for views in Relative Layout
 */
fun View.endRelativeMargin(size: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        marginEnd = size
    }
    this.layoutParams = params

}


/**
 * Sets bottom margin for views in Relative Layout
 */
fun View.bottomRelativeMargin(size: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop, marginRight, size)
    }
    this.layoutParams = params

}

/**
 * Sets top margin for views in Relative Layout
 */
fun View.topRelativeMargin(size: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }
    this.layoutParams = params

}


/**
 * Sets top margin for views in Relative Layout
 */
fun View.startRelativeMargin(size: Int) {
    val params = layoutParams as LayoutParams?

    params?.apply {
        marginStart = size
    }
    this.layoutParams = params

}


/**
 * Sets margins for views
 */
fun View.setMargins(size: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(size)
    }
    this.layoutParams = params

}


/**
 * Sets right margin for views
 */
fun View.endMargin(size: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        marginEnd = size
    }
    this.layoutParams = params

}


/**
 * Sets bottom margin for views
 */
fun View.bottomMargin(size: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop, marginRight, size)
    }
    this.layoutParams = params

}

/**
 * Sets top margin for views
 */
fun View.topMargin(size: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }
    this.layoutParams = params

}


/**
 * Sets top margin for views
 */
fun View.startMargin(size: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        marginStart = size
    }
    this.layoutParams = params

}


