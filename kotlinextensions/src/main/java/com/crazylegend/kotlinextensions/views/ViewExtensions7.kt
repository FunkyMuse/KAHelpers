package com.crazylegend.kotlinextensions.views

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

/**
 * Created by Hristijan, date 5/26/21
 */

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
fun FloatingActionButton.setTint(color: Int) {
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
val View.getActivity: Activity?
    get() {
        if (context is Activity)
            return context as Activity
        return null
    }

/**
 * Gives focus to the passed view once the view has been completely inflated
 */
fun Fragment.setFocusToView(view: View) {
    val handler = Handler(this.requireActivity().mainLooper)
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
    val handler = Handler(this.requireActivity().mainLooper)
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

fun View.margins(
    leftMargin: Int = Int.MAX_VALUE,
    topMargin: Int = Int.MAX_VALUE,
    rightMargin: Int = Int.MAX_VALUE,
    bottomMargin: Int = Int.MAX_VALUE
): View {
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


fun View.animateWidth(targetValue: Int, duration: Long = 400, action: ((Float) -> Unit)? = null) {
    ValueAnimator.ofInt(width, targetValue).apply {
        addUpdateListener {
            setWidth(it.animatedValue as Int)
            action?.invoke((it.animatedFraction))
        }
        setDuration(duration)
        start()
    }
}

fun View.animateHeight(targetValue: Int, duration: Long = 400, action: ((Float) -> Unit)? = null) {
    ValueAnimator.ofInt(height, targetValue).apply {
        addUpdateListener {
            setHeight(it.animatedValue as Int)
            action?.invoke((it.animatedFraction))
        }
        setDuration(duration)
        start()
    }
}


fun View.animateWidthAndHeight(
    targetWidth: Int,
    targetHeight: Int,
    duration: Long = 400,
    action: ((Float) -> Unit)? = null
) {
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
        throw RuntimeException("When calling this method, please make sure the View has been measured. If the width and height are 0, an exception is thrown as a reminder！")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

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
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
            )
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


/**
 * Attaches a listener to the recyclerview to hide the fab when it is scrolling downwards
 * The fab will reappear when scrolling has stopped or if the user scrolls up
 */
fun FloatingActionButton.hideOnDownwardsScroll(recycler: RecyclerView) {
    recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && !isShown) show()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 && isShown) hide()
            else if (dy < 0 && isOrWillBeHidden) show()
        }
    })
}

inline var View.scaleXY
    get() = Math.max(scaleX, scaleY)
    set(value) {
        scaleX = value
        scaleY = value
    }


fun View.elevate(elevation: Float) = setElevation(elevation)

val View.isAttachedToAWindow: Boolean
    get() {
        return isAttachedToWindow
    }

fun View.isInBounds(container: View): Boolean {
    val containerBounds = Rect()
    container.getHitRect(containerBounds)
    return getLocalVisibleRect(containerBounds)
}

/**
 * Creates an on touch listener that only emits on a short single tap
 */
@SuppressLint("ClickableViewAccessibility")
inline fun View.setOnSingleTapListener(crossinline onSingleTap: (v: View, event: MotionEvent) -> Unit) {
    setOnTouchListener { v, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                if (event.eventTime - event.downTime < 100)
                    onSingleTap(v, event)
                true
            }
            else -> false
        }
    }
}

fun BottomSheetBehavior<*>.onSlide(onSlide: (bottomSheet: View, slideOffset: Float) -> Unit = { _, _ -> }): BottomSheetBehavior.BottomSheetCallback {
    val listener = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlide(bottomSheet, slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
        }

    }
    addBottomSheetCallback(listener)
    return listener
}

fun BottomSheetBehavior<*>.onStateChanged(onStateChanged: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> }): BottomSheetBehavior.BottomSheetCallback {
    val listener = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            onStateChanged(bottomSheet, newState)
        }

    }
    addBottomSheetCallback(listener)
    return listener
}

fun View.getLocationOnScreen() = IntArray(2).apply { getLocationOnScreen(this) }

fun View.getLocationInWindow() = IntArray(2).apply { getLocationInWindow(this) }

operator fun TabLayout.get(position: Int): TabLayout.Tab = getTabAt(position)!!

inline fun TabLayout.forEach(func: (TabLayout.Tab) -> Unit) {
    for (i in 0 until tabCount) func(get(i))
}

fun TabLayout.tint(
    selectedPosition: Int = 0,
    selectedColor: Int = ContextCompat.getColor(context, android.R.color.white),
    defaultColor: Int = Color.parseColor("#80FFFFFF")
) {
    forEach { it.icon?.tint = defaultColor }
    get(selectedPosition).icon?.tint = selectedColor
}

fun TabLayout.hideTitles() = forEach {
    it.contentDescription = it.text
    it.text = ""
}

fun TabLayout.setIcons(icons: List<Drawable>) {
    for (i in 0 until tabCount) get(i).icon = icons[i]
    tint()
}

fun TabLayout.setIcons(@DrawableRes icons: Array<Int>) {
    for (i in 0 until tabCount) get(i).icon = ContextCompat.getDrawable(context, icons[i])
    tint()
}

fun TabLayout.setIcons(icons: TypedArray) {
    for (i in 0 until tabCount) get(i).icon = icons.getDrawable(i)
    tint()
}

fun TabLayout.getTabViewAt(position: Int) = (getChildAt(0) as ViewGroup).getChildAt(position)

fun Array<View>.gone() {
    forEach {
        it.gone()
    }
}

fun ProgressBar.indeterminateDrawableColor(@ColorRes color: Int) {
    indeterminateDrawable.setColorFilter(
        ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN
    )
}

fun View.aspect(ratio: Float = 3 / 4f) =
    post {
        val params = layoutParams
        params.height = (width / ratio).toInt()
        layoutParams = params
    }


fun View.waitForLayout(onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onGlobalLayoutListener.onGlobalLayout()
        }
    })
}


