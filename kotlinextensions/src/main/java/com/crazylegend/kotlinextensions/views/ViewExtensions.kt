package com.crazylegend.kotlinextensions.views

import android.animation.*
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.RelativeLayout.*
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.context.actionBarItemBackgroundResource
import com.crazylegend.kotlinextensions.context.drawable
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.context.selectableItemBackgroundResource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

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


inline fun TextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

inline fun AppCompatTextView.setTextSizeRes(@DimenRes rid: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

inline fun View.px(@DimenRes rid: Int): Int {
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
fun View.alignParentStart() {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_START)
    }

}


/**
 * Aligns to right of the parent in relative layout
 */
fun View.alignParentEnd() {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        addRule(ALIGN_PARENT_END)
    }

}


/**
 * Aligns in the center of the parent in relative layout
 */
fun View.alignInCenter() {
    val params = layoutParams as RelativeLayout.LayoutParams?

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
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(left, top, right, bottom)
    }
    this.layoutParams = params

}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(size: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(size)
    }
    this.layoutParams = params

}


/**
 * Sets right margin for views in Relative Layout
 */
fun View.endRelativeMargin(size: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        marginEnd = size
    }
    this.layoutParams = params

}


/**
 * Sets bottom margin for views in Relative Layout
 */
fun View.bottomRelativeMargin(size: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, marginTop, marginRight, size)
    }
    this.layoutParams = params

}

/**
 * Sets top margin for views in Relative Layout
 */
fun View.topRelativeMargin(size: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams?

    params?.apply {
        setMargins(marginLeft, size, marginRight, marginBottom)
    }
    this.layoutParams = params

}


/**
 * Sets top margin for views in Relative Layout
 */
fun View.startRelativeMargin(size: Int) {
    val params = layoutParams as RelativeLayout.LayoutParams?

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

fun BottomSheetBehavior<*>.onSlide(onSlide: (bottomSheet: View, slideOffset: Float) -> Unit = { _, _ -> }) {
    setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlide(bottomSheet, slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
        }

    })
}

fun BottomSheetBehavior<*>.onStateChanged(onStateChanged: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> }) {
    setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            onStateChanged(bottomSheet, newState)
        }

    })
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
            ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN
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


private const val DEFAULT_DRAWER_GRAVITY = GravityCompat.START

val DrawerLayout?.isOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.START) ?: false
val DrawerLayout?.isEndOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.END) ?: false
fun DrawerLayout?.open() = this?.openDrawer(GravityCompat.START)
fun DrawerLayout?.openEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.close() = this?.closeDrawer(GravityCompat.START)
fun DrawerLayout?.closeEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.toggle() = if (isOpen) close() else open()
fun DrawerLayout?.toggleEnd() = if (isEndOpen) closeEnd() else closeEnd()

inline fun DrawerLayout.consume(gravity: Int = GravityCompat.START, func: () -> Unit): Boolean {
    func()
    close()
    return true
}

fun View.setLightStatusBar(condition: Boolean = true) {
    if (Build.VERSION.SDK_INT >= 23 && condition) {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

val Context.hasNavigationBar: Boolean
    get() {
        return !ViewConfiguration.get(this).hasPermanentMenuKey()
    }

/**
 * Return true if navigation bar is at the bottom, false otherwise
 */
val Context.isNavigationBarHorizontal: Boolean
    get() {
        if (!hasNavigationBar) return false
        val dm = resources.displayMetrics
        return !navigationBarCanChangeItsPosition || dm.widthPixels < dm.heightPixels
    }

/**
 * Return true if navigation bar change its position when device rotates, false otherwise
 */
val Context.navigationBarCanChangeItsPosition: Boolean // Only phone between 0-599dp can
    get() {
        val dm = resources.displayMetrics
        return dm.widthPixels != dm.heightPixels && resources.configuration.smallestScreenWidthDp < 600
    }

/**
 * Return the status bar height. 0 otherwise
 */
val Context.statusBarHeight: Int
    get() {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(id)
    }

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) {
    setOnClickListener { func() }
}

inline fun <T : View> T.onLongClick(crossinline func: T.() -> Unit) {
    setOnLongClickListener { func(); true }
}

inline fun <T : View> T.onGlobalLayout(crossinline func: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                func()
            }
        }
    })
}

inline fun <T : View> T.onPreDraw(crossinline func: T.() -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            func()
            return true
        }
    })
}


fun View.changeBackgroundColor(@ColorInt newColor: Int, duration: Int = 300) {
    val oldBackground = background
    val color = ColorDrawable(newColor)
    val ld = LayerDrawable(arrayOf<Drawable>(color))
    if (oldBackground == null) background = ld
    else {
        val td = TransitionDrawable(arrayOf(oldBackground, ld))
        background = td
        td.startTransition(duration)
    }
}


inline fun Snackbar.maxLines(lines: Int): Snackbar {
    view.findViewById<TextView>(R.id.snackbar_text).maxLines = lines
    return this
}


/**
 * Sets receiver's visibility to [View.GONE] if [TextView.getText] is
 * null or empty; sets it to [View.VISIBLE] otherwise.
 */
fun TextView.collapseIfEmpty() {
    visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.GONE
}

/**
 * Sets receiver's visibility to [View.INVISIBLE] if [TextView.getText] is
 * null or empty; sets it to [View.VISIBLE] otherwise.
 */
fun TextView.hideIfEmpty() {
    visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
}


/**
 * Displays a popup by inflating menu with specified
 * [menu resource id][menuResourceId], calling [onClick] when an item
 * is clicked, and optionally calling [onInit] with
 * [PopupMenu] as receiver to initialize prior to display.
 */
fun View.showPopup(
        @MenuRes menuResourceId: Int,
        onInit: PopupMenu.() -> Unit = {},
        onClick: (MenuItem) -> Boolean
) {
    PopupMenu(context, this).apply {
        menuInflater.inflate(menuResourceId, menu)
        onInit(this)
        setOnMenuItemClickListener(onClick)
    }.show()
}

/**
 * Sets receiver's visibility to [View.INVISIBLE] if [value] is true;
 * sets it to [View.VISIBLE] otherwise. Opposite of [showIf]; also
 * see [collapseIf].
 */
fun View.hideIf(value: Boolean) {
    visibility = if (!value) View.VISIBLE else View.INVISIBLE
}

/**
 * Sets receiver's visibility to [View.GONE] if [value] is true;
 * sets it to [View.VISIBLE] otherwise. Opposite of [expandIf]; also
 * see [hideIf].
 */
fun View.collapseIf(value: Boolean) {
    visibility = if (!value) View.VISIBLE else View.GONE
}

inline fun <T : Adapter> AdapterView<T>.onItemSelected(crossinline action: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit = { _, _, _, _ -> }) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                action(parent, view, position, id)
    }
}

/**
 * Returns the default, clear background for selectable items.  Reacts when touched.
 */
val View.selectableItemBackgroundResource: Int
    get() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return outValue.resourceId
    }

/**
 * Returns the default, clear background for selectable items without a border.  Reacts when touched.
 */
val View.selectableItemBackgroundBorderlessResource: Int
    get() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        return outValue.resourceId
    }

/**
 * Allows you to modify the elevation on a view without worrying about version.
 */
var View.elevationCompat: Float
    get() {
        return elevation
    }
    set(value) {
        elevation = value
    }

/**
 * Shows the soft input for the vindow.
 */
fun View.showSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }.showSoftInput(this, 0)
}

/**
 * Hides the soft input for the vindow.
 */
fun View.hideSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }
            .hideSoftInputFromWindow(this.applicationWindowToken, 0)
}

/**
 * Sets an on click listener for a view, but ensures the action cannot be triggered more often than [coolDown] milliseconds.
 */
inline fun View.setOnClickListenerCooldown(coolDown: Long = 1000L, crossinline action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastTime = 0L
        override fun onClick(v: View?) {
            val now = System.currentTimeMillis()
            if (now - lastTime > coolDown) {
                action()
                lastTime = now
            }
        }
    })
}

/**
 * Post functions
 */
inline fun <T : View> T.postLet(crossinline block: (T) -> Unit) {
    post { block(this) }
}

inline fun <T : View> T.postDelayedLet(delay: Long, crossinline block: (T) -> Unit) {
    postDelayed({ block(this) }, delay)
}

inline fun <T : View> T.postApply(crossinline block: T.() -> Unit) {
    post { block(this) }
}

inline fun <T : View> T.postDelayedApply(delay: Long, crossinline block: T.() -> Unit) {
    postDelayed({ block(this) }, delay)
}

fun TabLayout.addTab(@StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    addTab(newTab().setCustomView(tab))
}

fun TabLayout.updateTabAt(position: Int, @StringRes title: Int, @DrawableRes icon: Int, @LayoutRes customView: Int) {
    val tab = LayoutInflater.from(context).inflate(customView, this as ViewGroup, false) as TextView
    tab.setText(title)
    tab.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    getTabAt(position)?.customView = tab
}

fun TabLayout.Tabs(): List<TabLayout.Tab> {

    val tabs = mutableListOf<TabLayout.Tab>()

    (0..tabCount).forEach { index: Int ->
        getTabAt(index)?.let { tabs.add(it) }
    }

    return tabs
}

fun TextInputLayout.setTextInputLayoutUpperHintColor(@ColorInt color: Int) {
    defaultHintTextColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
}


fun TextInputLayout.toggleTextHintColorOnEmpty(activeColor: Int, inactiveColor: Int) = setTextInputLayoutUpperHintColor(
        if (editText?.text?.isNotEmpty() == true)
            activeColor else
            inactiveColor
)

fun View.afterLatestMeasured(callback: () -> Unit) {
    this.post {
        callback()
    }
}

fun View.isLaidOutCompat(): Boolean {
    return ViewCompat.isLaidOut(this)
}

fun View.setbackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
}

fun View.toggleVisibility() {
    if (isVisible) gone() else visible()
}


infix fun View.and(v: View): List<View> {
    return mutableListOf(this, v)
}

infix fun List<View>.and(v: View): List<View> {
    val list = mutableListOf<View>()
    list.addAll(this)
    list.add(v)
    return list
}

fun List<View>.gone() {
    this.forEach { it.gone() }
}

fun List<View>.invisible() {
    this.forEach { it.invisible() }
}

fun List<View>.visible() {
    this.forEach { it.visible() }
}

fun View.setBackgroundTintRes(@ColorRes colorRes: Int, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER) {
    this.background.setColorFilter(context.getColorCompat(colorRes), tintMode)
}

fun View.setBackgroundTint(color: Int, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER) {
    this.background.setColorFilter(color, tintMode)
}

fun SearchView.textListener(
        onQuerySubmit: (queryTextSubmit: String) -> Unit = { _ -> },
        onQueryChange: (queryTextChange: String) -> Unit = { _ -> }
) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQuerySubmit(query.toString())
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            onQueryChange(query.toString())
            return true
        }
    })
}

val SearchView?.getSubmitButton get() = this?.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)


fun BottomSheetBehavior<*>.sliderListener(
        onSlide: (bottomSheet: View, slideOffset: Float) -> Unit = { _, _ -> },
        onStateChanged: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> }
) {

    this.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlide(bottomSheet, slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            onStateChanged(bottomSheet, newState)
        }
    })

}

fun View.enable() {
    this.isEnabled = true
}

fun View.toggleEnabled() {
    this.isEnabled = !this.isEnabled
}

fun View.disable() {
    this.isEnabled = false
}

inline fun View.toggleSelected() {
    this.isSelected = !this.isSelected
}

fun View.windowBackground(): Int {
    return context.themeAttributeToColor(android.R.attr.windowBackground)
}

// Used to tint buttons
fun Context.textColorTertiary(): Int {
    return this.themeAttributeToColor(android.R.attr.textColorTertiary)
}

fun MenuItem.disable() {
    this.isEnabled = false
}

fun MenuItem.enable() {
    this.isEnabled = true
}

fun MenuItem.toggleEnabled() {
    this.isEnabled = !this.isEnabled
}

fun MenuItem.check() {
    this.isChecked = true
}

fun MenuItem.unCheck() {
    this.isChecked = false
}

fun MenuItem.toggleChecked() {
    this.isChecked = !this.isChecked
}

fun Context.themeAttributeToColor(themeAttributeId: Int, fallbackColor: Int = Color.WHITE): Int {
    val outValue = TypedValue()
    val theme = this.theme
    val resolved = theme.resolveAttribute(themeAttributeId, outValue, true)
    if (resolved) {
        return ContextCompat.getColor(this, outValue.resourceId)
    }
    return fallbackColor
}

fun SeekBar.onProgressChanged(callback: (theSeekBar: SeekBar, progress: Int, fromUser: Boolean) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) =
                callback(seekBar, progress, fromUser)
    })
}

fun ProgressBar.loaderColor(color: Int) {
    this.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun CompoundButton.onChecked(onChecked: (View, Boolean) -> Unit) {
    setOnCheckedChangeListener { buttonView, isChecked ->
        onChecked(buttonView, isChecked)
    }
}

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun View.snackbar(
        message: Int, duration: Int = Snackbar.LENGTH_SHORT,
        actionName: Int = 0, actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != 0 && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
        message: Int, duration: Int = Snackbar.LENGTH_SHORT,
        actionName: String = "", actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != "" && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
        message: String, duration: Int = Snackbar.LENGTH_SHORT,
        actionName: Int = 0, actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != 0 && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.snackbar(
        message: String, duration: Int = Snackbar.LENGTH_SHORT,
        actionName: String = "", actionTextColor: Int = 0, action: (View) -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, duration)

    if (actionName != "" && action != {}) snackbar.setAction(actionName, action)
    if (actionTextColor != 0) snackbar.setActionTextColor(actionTextColor)

    snackbar.show()
    return snackbar
}

fun View.animateTranslationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateTranslationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateTranslationZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateAlpha(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotation(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.translationXAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.translationYAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.translationZAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleXAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleYAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.alphaAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationXAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationYAnimator(
        values: FloatArray,
        duration: Long = 300,
        repeatCount: Int = 0,
        repeatMode: Int = 0
): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.xAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.yAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.zAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}


fun ViewGroup.isEmpty() = childCount == 0

fun ViewGroup.isNotEmpty() = !isEmpty()

/**
 * get Activity On Which View is inflated to
 */
fun View.getActivity(): Activity? {
    if (context is Activity)
        return context as Activity
    return null
}

/**
 * will show the view If Condition is true else make if INVISIBLE or GONE Based on the [makeInvisible] flag
 */
fun View.showIf(boolean: Boolean, makeInvisible: Boolean = false) {
    visibility = if (boolean) View.VISIBLE else if (makeInvisible) View.INVISIBLE else View.GONE
}


/**
 * will enable the view If Condition is true else enables It
 */

fun View.enableIf(boolean: Boolean) = { isEnabled = boolean }

/**
 * will disable the view If Condition is true else enables It
 */

fun View.disableIf(boolean: Boolean) = { isEnabled = boolean.not() }

val unspecified
    get() = View.MeasureSpec.UNSPECIFIED

val atMost
    get() = View.MeasureSpec.AT_MOST

val exactly
    get() = View.MeasureSpec.EXACTLY


var View.transitionNameCompat: String?
    get() = ViewCompat.getTransitionName(this)
    set(value) = ViewCompat.setTransitionName(this, value)


@TargetApi(value = Build.VERSION_CODES.M)
fun View.setRippleClickForeground() {
    if (canUseForeground) {
        foreground = context.drawable(context.selectableItemBackgroundResource)
        setClickable()
    }
}

fun View.setRippleClickBackground() {
    setBackgroundResource(context.selectableItemBackgroundResource)
    setClickable()
}

fun View.setRippleClickAnimation() =
        if (canUseForeground) setRippleClickForeground() else setRippleClickBackground()


private val canUseForeground
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


fun View.setClickable() {
    isClickable = true
    isFocusable = true
}

fun View.setRoundRippleClickBackground() {
    setBackgroundResource(context.actionBarItemBackgroundResource)
    setClickable()
}

fun View.setRoundRippleClickAnimation() = setRoundRippleClickBackground()


fun View.rootView(): View {
    var root = this
    while (root.parent is View) {
        root = root.parent as View
    }
    return root
}


fun View.resetFocus() {
    clearFocus()
    isFocusableInTouchMode = false
    isFocusable = false
    isFocusableInTouchMode = true
    isFocusable = true
}

inline fun <reified V : View> V.onFirstAttachToWindow(crossinline whenAttached: V.() -> Unit) {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            removeOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(v: View?) {
            removeOnAttachStateChangeListener(this)
            (v as? V)?.whenAttached()
        }
    })
}

inline fun <reified T : View> ViewGroup.findView(): T? {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is T) {
            return view
        }
    }

    return null
}

var ViewGroup.animateLayoutChanges: Boolean
    set(value) {
        if (value) {
            layoutTransition = LayoutTransition().apply {
                disableTransitionType(LayoutTransition.DISAPPEARING)
            }
        } else {
            layoutTransition = null
        }
    }
    get() = layoutTransition != null


fun View.setSize(height: Int, width: Int) {
    val params = layoutParams
    params.width = width
    params.height = height
    layoutParams = params
}


fun ViewGroup.getString(@StringRes stringRes: Int): String {
    return this.context.getString(stringRes)
}


fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams


    when (params) {
        is ConstraintLayout.LayoutParams -> {
            params.setMargins(left, top, right, bottom)
            this.layoutParams = params
        }
        is LinearLayout.LayoutParams -> {
            params.setMargins(left, top, right, bottom)
            this.layoutParams = params
        }
        is FrameLayout.LayoutParams -> {
            params.setMargins(left, top, right, bottom)
            this.layoutParams = params
        }
        is RelativeLayout.LayoutParams -> {
            params.setMargins(left, top, right, bottom)
            this.layoutParams = params
        }
    }
}

fun View.modifyMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val l = left ?: this.marginLeft
    val t = top ?: this.marginTop
    val r = right ?: this.marginRight
    val b = bottom ?: this.marginBottom
    this.setMargins(l, t, r, b)
}

fun View.modifyPadding(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val l = left ?: this.paddingLeft
    val t = top ?: this.paddingTop
    val r = right ?: this.paddingRight
    val b = bottom ?: this.paddingBottom
    this.setPadding(l, t, r, b)
}


fun View.scale(scale: Float) {
    this.scaleX = scale
    this.scaleY = scale
}

fun View.setWidthWrapContent() {
    val params = this.layoutParams
    params.width = ViewGroup.LayoutParams.WRAP_CONTENT
    this.layoutParams = params
}

fun View.setHeightWrapContent() {
    val params = this.layoutParams
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    this.layoutParams = params
}

fun View.disableClipOnParents() {
    val v = this

    if (v.parent == null) {
        return
    }

    if (v is ViewGroup) {
        v.clipChildren = false
    }

    if (v.parent is View) {
        (v.parent as View).disableClipOnParents()
    }
}

fun View.getGoneHeight(): Int {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(widthSpec, heightSpec)
    return this.measuredHeight
}

fun View.setSemiTransparentIf(shouldBeTransparent: Boolean, disabledAlpha: Float = 0.3f) {
    alpha = when (shouldBeTransparent) {
        true -> disabledAlpha
        false -> 1f
    }
}

fun ViewGroup.subviews(vararg views: View): View {
    assignViewIdIfNeeded()
    for (v in views) {
        v.assignViewIdIfNeeded()
        addView(v)
    }
    return this
}

fun View.assignViewIdIfNeeded() {
    if (id == -1) {
        id = View.generateViewId()
    }
}

fun View.getGoneHeight(callback: (futureHeight: Int) -> Unit) {


    afterLatestMeasured {
        val originalHeight = height // save the original height (is most likely wrap content)

        setHeight(0) // "hide" the view
        invisible() // make the view invisible so it gets a width

        this.afterLatestMeasured {

            val originalWidth = width // the view now has a width

            // measure how high the view will be
            val widthSpec = View.MeasureSpec.makeMeasureSpec(originalWidth, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            measure(widthSpec, heightSpec)

            val futureHeight = measuredHeight

            // hide the view and set back to the original height
            gone()
            setHeight(originalHeight)
            callback(futureHeight)
        }
    }
}



fun layout(vararg items: Any) {
    var previousMargin: Int? = null
    var previousView: View? = null
    var viewCount = 0
    for (item in items) {

        fun layoutView(view: View) {
            previousMargin?.let { previousMargin ->
                if (viewCount == 1) {
                    view.top(previousMargin)
                } else {
                    previousView?.let { previousView ->
                        view.constrainTopToBottomOf(previousView, previousMargin)
                    }
                }
            }
            previousView = view
        }

        // Embedded Horizontal layout.
        (item as? Array<Any>)?.let { horizontalLayout ->

            // Take first "View" type in the array to layout.
            var secondItem = if (horizontalLayout.count() > 1) horizontalLayout[1] else null
            var firstView = (horizontalLayout.firstOrNull() as? View)
                    ?: (secondItem as? View)
            firstView?.let {
                layoutView(it)
            }
        }

        when (item) {
            is Int -> {
                previousMargin = item
                if (viewCount == items.count() - 1) { // Last Margin == Bottom
                    previousView?.let { previousView ->
                        previousView.bottom(item)
                    }
                }
            }
            is View -> {
                layoutView(item)
            }
            is String -> {
                previousMargin = null
                previousView = null
            }
        }
        viewCount++
    }
}

