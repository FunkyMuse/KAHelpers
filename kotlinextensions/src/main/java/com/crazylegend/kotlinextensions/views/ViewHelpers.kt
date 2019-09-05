package com.crazylegend.kotlinextensions.views

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crazylegend.kotlinextensions.bitmap.flipHorizontally
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.context.getCompatColor


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

fun ImageView.flipHorizontically() {
    setImageBitmap(getBitmap().flipHorizontally())
}

fun View.setLayoutHeight(height: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams
    params.height = height
    layoutParams = params
}

fun SwipeRefreshLayout.setIsRefreshing() {
    isRefreshing = true
}

fun SwipeRefreshLayout.setIsNotRefreshing() {
    isRefreshing = false
}


fun ArrayList<View>?.setFont(font: Typeface?) {
    if (font == null)
        return

    if (this == null)
        return

    val textViews = ArrayList<TextView>()
    for (view in this) {
        if (view is TextView)
            textViews.add(view)
    }

    for (v in textViews)
        v.typeface = font
}

fun getScreenLocation(view: View): IntArray {
    val locations = IntArray(2)
    view.getLocationOnScreen(locations)
    return locations
}

fun View.moveViewRelatively(left: Int, top: Int) {
    val location = getScreenLocation(this)
    val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    params.setMargins(left + location[0], top + location[1], 0, 0)
    val p = layoutParams
    params.width = p.width
    params.height = p.height
    layoutParams = params
}

fun View.moveView(left: Int, top: Int) {
    val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    params.setMargins(left, top, 0, 0)
    val p = layoutParams
    params.width = p.width
    params.height = p.height
    layoutParams = params
}

fun List<TextView>.setFont(font: Typeface) {
    for (v in this)
        v.typeface = font
}

fun getDensityDpi(): Int {
    return Resources.getSystem().displayMetrics.densityDpi
}

fun getDensity(): Float {
    return Resources.getSystem().displayMetrics.density
}

fun Context.getScaledDrawable(@DrawableRes resourceId: Int, scaleInDp: Int): Drawable {

    fun roundToInt(v: Int): Int {
        return (v + 0.5).toInt()
    }


    fun dpToPx(dp: Int): Int {
        val scale = getDensity()
        return (dp * scale + 0.5f).toInt()
    }

    val options = BitmapFactory.Options()
    val metrics = resources.displayMetrics
    options.inScreenDensity = metrics.densityDpi
    options.inTargetDensity = metrics.densityDpi
    options.inDensity = DisplayMetrics.DENSITY_DEFAULT
    val px = roundToInt(dpToPx(scaleInDp))
    val bitmap = BitmapFactory.decodeResource(this.resources, resourceId, options)
    val drawable = BitmapDrawable(this.resources, Bitmap.createScaledBitmap(bitmap, px, px, true))

    bitmap.recycle()
    return drawable
}

/**
 * AndroidX version of searchview gets the disabled icon hint
 * @receiver SearchView
 */
fun SearchView.disableSearchIconHint(){
    try {
        val mDrawable = SearchView::class.java.getDeclaredField("mSearchHintIcon")
        mDrawable.isAccessible = true
        val drawable = mDrawable.get(this) as Drawable
        drawable.setBounds(0, 0, 0, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.getContentRoot(): View {
    return window
            .decorView
            .findViewById(android.R.id.content)
}


fun getLocationOnScreen(textView: View): Rect {
    val rect = Rect()
    val location = IntArray(2)

    textView.getLocationOnScreen(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + textView.width
    rect.bottom = location[1] + textView.height

    return rect
}

fun hideOnLostFocus(event: MotionEvent, vararg views: View) {

    var hit = false

    for (view in views)
        hit = hit or getLocationOnScreen(view).contains(event.x.toInt(), event.y.toInt())

    if (event.action == MotionEvent.ACTION_DOWN && !hit)
        views[0].hideSoftInput()
}

fun View.setViewBackgroundWithoutResettingPadding(@DrawableRes backgroundResId: Int) {
    setBackgroundResource(backgroundResId)
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

fun View.setViewBackgroundColorWithoutResettingPadding(color: Int) {
    val paddingBottom = this.paddingBottom
    val paddingLeft = this.paddingLeft
    val paddingRight = this.paddingRight
    val paddingTop = this.paddingTop
    this.setBackgroundColor(this.context.getCompatColor(color))
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}


fun TextView.addFadeOutToText(galleryHeight: Int, @ColorRes fromColor: Int, @ColorRes toColor: Int) {

    val bounds = Rect()
    val textPaint = paint
    val text = text
    textPaint.getTextBounds(text.toString(), 0, text.length, bounds)
    val viewWidth = width
    val startHeight = if (lineCount > 2) galleryHeight / 2 else 0
    val availableWidth =
            (viewWidth - paddingLeft - paddingRight - textPaint.measureText(text.toString())).toInt()
    if (availableWidth < 0) {
        val textShader = LinearGradient(
                (3 * viewWidth / 4).toFloat(), startHeight.toFloat(), viewWidth.toFloat(), paint.textSize,
                intArrayOf(this.context.getColorCompat(fromColor), this.context.getColorCompat(toColor)), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
    }
}


/**
 * gradient(200f, 0x80C24641.toInt(), 0x80FFFFFF.toInt())
 */
fun View.gradient(radius: Float, vararg colors: Int) {
    background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply { cornerRadius = radius }
}

fun View.locationInWindow(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationInWindow(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

class ClickSpan(val listener: View.OnClickListener?) : ClickableSpan() {

    override fun onClick(widget: View) {
        listener?.onClick(widget)
    }
}

fun TextView.clickify(clickableText: String, listener: View.OnClickListener) {
    val text = text
    val string = text.toString()
    val span = ClickSpan(listener)

    val start = string.indexOf(clickableText)
    val end = start + clickableText.length
    if (start == -1) return

    if (text is Spannable) {
        text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    } else {
        val s = SpannableString.valueOf(text)
        s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(s)
    }

    val m = movementMethod
    if (m == null || m !is LinkMovementMethod) {
        movementMethod = LinkMovementMethod.getInstance()
    }
}


fun View.locationOnScreen(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationOnScreen(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

@ColorInt
fun View.getCurrentColor(@ColorInt default: Int = Color.TRANSPARENT): Int = (background as? ColorDrawable)?.color
        ?: default
