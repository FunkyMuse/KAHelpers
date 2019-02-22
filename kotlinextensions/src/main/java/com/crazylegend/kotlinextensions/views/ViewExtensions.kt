package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.UiThread
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.material.textfield.TextInputEditText

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

val TextInputEditText.getString: String get() = this.text.toString()
val TextInputEditText.getStringTrimmed: String get() = this.text.toString().trim()

fun TextInputEditText.setTheText(text: String) {
    this.setText(text, TextView.BufferType.EDITABLE)
}


val EditText.getString: String get() = this.text.toString()
val EditText.getStringTrimmed: String get() = this.text.toString().trim()


fun EditText.setTheText(text: String) {
    this.setText(text, TextView.BufferType.EDITABLE)
}

val SearchView?.editTextSearchView get() = this?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)


private var viewOriginalHeight: Int = 0

fun Button.enableButtonWithLoading( progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
    if (this.tag != null) {
        this.text = this.tag.toString()
    }
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButtonWithLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
    this.tag = this.text
    this.text = ""
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

fun Button.enableButton() {
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButton() {
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

fun collapseLayout(linearLayout: LinearLayout, imageView: ImageView, dropUPIMG:Int, dropDOWNIMG:Int) {
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
    val alpha = Math.round(Color.alpha(color) * factor)
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
fun View.toggleVisibilityInvisibleToVisible() : View {
    visibility = if (visibility == View.VISIBLE) {
        View.INVISIBLE
    } else {
        View.INVISIBLE
    }
    return this
}

/**
 * INVISIBLE TO GONE AND OTHERWISE
 */
fun View.toggleVisibilityGoneToVisible() : View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.GONE
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
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

