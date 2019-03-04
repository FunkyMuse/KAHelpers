package com.crazylegend.kotlinextensions.views

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.UiThread
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

private var viewOriginalHeight: Int = 0

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
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
fun View.toggleVisibilityInvisibleToVisible(): View {
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
fun View.toggleVisibilityGoneToVisible(): View {
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
        visibility = View.VISIBLE
    }
}

/**
 * Invisible if condition met
 */

inline fun View.invisibleIf(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        visibility = View.INVISIBLE
    }
}

/**
 * Gone if condition met
 */
inline fun View.goneIf(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        visibility = View.GONE
    }
}


/**
 * Turns refreshing off on SwipeTo refresh layout
 */
fun SwipeRefreshLayout.turnOff() = setOnRefreshListener { isRefreshing = false }


/**
 * Aligns to left of the parent in relative layout
 */
fun View.alignParentStart(){
   val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_START)
    }

}



/**
 * Aligns to right of the parent in relative layout
 */
fun View.alignParentEnd(){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_END)
    }

}




/**
 * Aligns in the center of the parent in relative layout
 */
fun View.alignInCenter(){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        addRule(CENTER_HORIZONTAL)
    }

}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(left:Int, top:Int, right:Int, bottom:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
       setMargins(left, top, right, bottom)
    }

}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(size:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
       setMargins(size)
    }

}


/**
 * Sets right margin for views in Linear Layout
 */
fun View.endLinearMargin(size:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
       marginEnd = size
    }

}




/**
 * Sets bottom margin for views in Linear Layout
 */
fun View.bottomLinearMargin(size:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop , marginRight, size)
    }

}

/**
 * Sets top margin for views in Linear Layout
 */
fun View.topLinearMargin(size:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }

}



/**
 * Sets top margin for views in Linear Layout
 */
fun View.startLinearMargin(size:Int){
    val params =  layoutParams as LinearLayout.LayoutParams?

    params?.apply {
       marginStart = size
    }

}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(left:Int, top:Int, right:Int, bottom:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
       setMargins(left, top, right, bottom)
    }

}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(size:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
       setMargins(size)
    }

}


/**
 * Sets right margin for views in Relative Layout
 */
fun View.endRelativeMargin(size:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        marginEnd = size
    }

}




/**
 * Sets bottom margin for views in Relative Layout
 */
fun View.bottomRelativeMargin(size:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop , marginRight, size)
    }

}

/**
 * Sets top margin for views in Relative Layout
 */
fun View.topRelativeMargin(size:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }

}



/**
 * Sets top margin for views in Relative Layout
 */
fun View.startRelativeMargin(size:Int){
    val params =  layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        marginStart = size
    }

}

/**
 * Sets margins for views
 */
fun View.setMargins(left:Int, top:Int, right:Int, bottom:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(left, top, right, bottom)
    }
}


/**
 * Sets margins for views
 */
fun View.setMargins(size:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(size)
    }

}


/**
 * Sets right margin for views
 */
fun View.endMargin(size:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        marginEnd = size
    }

}




/**
 * Sets bottom margin for views
 */
fun View.bottomMargin(size:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop , marginRight, size)
    }

}

/**
 * Sets top margin for views
 */
fun View.topMargin(size:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }

}



/**
 * Sets top margin for views
 */
fun View.startMargin(size:Int){
    val params =  layoutParams as ViewGroup.MarginLayoutParams?

    params?.apply {
        marginStart = size
    }

}


/**
 * Gives focus to the passed view once the view has been completely inflated
 */
fun Activity.setFocusToView(view: View) {
    val handler = Handler(this.mainLooper)
    handler.post { view.requestFocus() }
}

/**
 * Change Floating action button tint
 */
fun FloatingActionButton.setTint(color:Int){
    this.imageTintList = ColorStateList.valueOf(color)
}


/**
 * Create a Screnshot of the view and returns it as a Bitmap
 */
fun View.screenshot(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}


/**
 * get Activity On Which View is inflated to
 */
val View.getActivity : Activity?  get() {
    if (context is Activity)
        return context as Activity
    return null
}

/**
 * Gives focus to the passed view once the view has been completely inflated
 */
fun Fragment.setFocusToView(view: View) {
    val handler = Handler(this.activity?.mainLooper)
    handler.post { view.requestFocus() }
}

/**
 * Gives focus to the passed view once the view has been completely
 * inflated using `view.requestFocusFromTouch`
 */
fun Activity.setTouchFocusToView(view: View) {
    val handler = Handler(this.mainLooper)
    handler.post { view.requestFocusFromTouch() }
}

/**
 * Gives focus to the passed view once the view has been completely
 * inflated using `view.requestFocusFromTouch`
 */
fun Fragment.setTouchFocusToView(view: View) {
    val handler = Handler(this.activity?.mainLooper)
    handler.post { view.requestFocusFromTouch() }
}

/**
 * Hides all the views passed as argument(s)
 */
fun Context.hideViews(vararg views: View) = views.forEach { it.visibility = View.GONE }

/**
 * Shows all the views passed as argument(s)
 */
fun Context.showViews(vararg views: View) = views.forEach { it.visibility = View.VISIBLE }


fun View.limitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

fun View.limitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

fun View.margin(leftMargin: Int = Int.MAX_VALUE, topMargin: Int = Int.MAX_VALUE, rightMargin: Int = Int.MAX_VALUE, bottomMargin: Int = Int.MAX_VALUE): View {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (leftMargin != Int.MAX_VALUE)
        params.leftMargin = leftMargin
    if (topMargin != Int.MAX_VALUE)
        params.topMargin = topMargin
    if (rightMargin != Int.MAX_VALUE)
        params.rightMargin = rightMargin
    if (bottomMargin != Int.MAX_VALUE)
        params.bottomMargin = bottomMargin
    layoutParams = params
    return this
}



fun View.animateWidth(targetValue: Int, duration: Long = 400, action:((Float)->Unit)? = null) {
    ValueAnimator.ofInt(width, targetValue).apply {
        addUpdateListener {
            setWidth(it.animatedValue as Int)
            action?.invoke((it.animatedFraction))
        }
        setDuration(duration)
        start()
    }
}

fun View.animateHeight(targetValue: Int, duration: Long = 400, action:((Float)->Unit)? = null) {
    ValueAnimator.ofInt(height, targetValue).apply {
        addUpdateListener {
            setHeight(it.animatedValue as Int)
            action?.invoke((it.animatedFraction))
        }
        setDuration(duration)
        start()
    }
}


fun View.animateWidthAndHeight(targetWidth: Int, targetHeight: Int, duration: Long = 400, action:((Float)->Unit)? = null) {
    val startHeight = height
    val evaluator = IntEvaluator()
    ValueAnimator.ofInt(width, targetWidth).apply {
        addUpdateListener {
            resize(it.animatedValue as Int, evaluator.evaluate(it.animatedFraction, startHeight, targetHeight))
            action?.invoke((it.animatedFraction))
        }
        setDuration(duration)
        start()
    }
}

/**
 * Get a screenshot of the View, support a long screenshot of the entire RecyclerView list
 * Note: When calling this method, please make sure the View has been measured. If the width and height are 0, an exception will be thrown.
 */
fun View.toBitmap(): Bitmap {
    if (measuredWidth == 0 || measuredHeight == 0) {
        throw RuntimeException("调用该方法时，请确保View已经测量完毕，如果宽高为0，则抛出异常以提醒！")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val bmp = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)

            //draw default bg, otherwise will be black
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            this.draw(canvas)
            // reset height
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
            bmp //return
        }
        else -> {
            val screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(screenshot)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)// Draw the view onto the canvas
            screenshot //return
        }
    }
}


