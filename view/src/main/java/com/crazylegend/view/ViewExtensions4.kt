package com.crazylegend.view

import android.animation.Animator
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.*
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Hristijan, date 5/26/21
 */


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


private fun Context.getResourceIdAttribute(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}


var View.transitionNameCompat: String?
    get() = ViewCompat.getTransitionName(this)
    set(value) = ViewCompat.setTransitionName(this, value)


@TargetApi(value = Build.VERSION_CODES.M)
fun View.setRippleClickForeground() {
    if (canUseForeground) {
        foreground = ContextCompat.getDrawable(context, context.getResourceIdAttribute(android.R.attr.selectableItemBackground))
        setClickable()
    }
}

fun View.setRippleClickBackground() {
    setBackgroundResource(context.getResourceIdAttribute(android.R.attr.selectableItemBackground))
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
    setBackgroundResource(context.getResourceIdAttribute(android.R.attr.actionBarItemBackground))
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


/**
 *  Request to be laid out fullscreen tell the system to lay out our app behind the system bars
 */
fun View.fullScreen() {
    systemUiVisibility =
            // Tells the system that the window wishes the content to
            // be laid out at the most extreme scenario. See the docs for
            // more information on the specifics
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                // Tells the system that the window wishes the content to
                // be laid out as if the navigation bar was hidden
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
}


fun View.dip(value: Int): Int = (value * (resources.displayMetrics.density)).toInt()
fun View.dimen(@DimenRes resourceId: Int): Float = resources.getDimension(resourceId)
fun View.integer(@IntegerRes resourceId: Int): Int = resources.getInteger(resourceId)
fun View.bool(@BoolRes resourceId: Int): Boolean = resources.getBoolean(resourceId)
fun View.colorStateList(@ColorRes resourceId: Int): ColorStateList? = ContextCompat.getColorStateList(context, resourceId)
fun View.drawable(@DrawableRes resourceId: Int): Drawable? = ContextCompat.getDrawable(context, resourceId)
fun View.drawable(@DrawableRes resourceId: Int, tintColorResId: Int): Drawable? =
    ContextCompat.getDrawable(context, resourceId)?.apply {
        setTint(color(tintColorResId))
    }


fun View.string(@StringRes resourceId: Int): String = resources.getString(resourceId)
fun View.string(@StringRes resourceId: Int, vararg args: Any?): String = resources.getString(resourceId, *args)
fun View.quantityString(@PluralsRes resourceId: Int, quantity: Int, vararg args: Any?): String = resources.getQuantityString(resourceId, quantity, quantity, *args)


fun View.snack(msg: CharSequence, @ColorRes colorResId: Int? = null,
               duration: Int = Snackbar.LENGTH_SHORT, build: (Snackbar.() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, msg, duration)
    colorResId?.let { snackbar.view.setBackgroundColor(color(colorResId)) }
    build?.let { snackbar.build() }
    snackbar.show()
}

val View.window: Window
    get() = activity.window


val View.activity: ComponentActivity
    get() = context as ComponentActivity


fun View.setNewHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setNewWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}


inline var View.bottomMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = value
    }

inline var View.topMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).topMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).topMargin = value
    }

inline var View.rightMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = value
    }

inline var View.leftMargin: Int
    get():Int {
        return (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
    }
    set(value) {
        (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = value
    }

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(left, top, right, bottom)
}

inline var View.leftPadding: Int
    get() = paddingLeft
    set(value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

inline var View.topPadding: Int
    get() = paddingTop
    set(value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

inline var View.rightPadding: Int
    get() = paddingRight
    set(value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

inline var View.bottomPadding: Int
    get() = paddingBottom
    set(value) = setPadding(paddingLeft, paddingTop, paddingRight, value)
