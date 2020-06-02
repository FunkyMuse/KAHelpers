package com.crazylegend.kotlinextensions.views

import android.annotation.TargetApi
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.AlphaAnimation
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.crazylegend.kotlinextensions.collections.use
import com.crazylegend.kotlinextensions.context.colorWithOpacity
import com.crazylegend.kotlinextensions.context.drawable
import com.crazylegend.kotlinextensions.context.inputManager
import com.crazylegend.kotlinextensions.context.selectableItemBackgroundResource
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


private fun ViewGroup.enableDisableChildren(enable: Boolean): ViewGroup = apply {
    (0 until childCount).forEach {
        when (val view = getChildAt(it)) {
            is ViewGroup -> view.enableDisableChildren(enable)
            else -> if (enable) view.enable() else view.disable()
        }
    }
}

fun ViewGroup.disableChildren() = enableDisableChildren(false)
fun ViewGroup.enableChildren() = enableDisableChildren(true)

fun View.hideIme() {
    val imm = context.inputManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Get color from resources with alpha
 */
@ColorInt
@Deprecated(message = "Use new [colorWithOpacity] extension", replaceWith = ReplaceWith("colorWithOpacity(res, alphaPercent)"))
fun View.colorWithAlpha(@ColorRes res: Int, @IntRange(from = 0, to = 100) alphaPercent: Int): Int {
    return colorWithOpacity(res, alphaPercent)
}

/**
 * Get color from resources with applied [opacity]
 */
@ColorInt
fun View.colorWithOpacity(@ColorRes res: Int, @IntRange(from = 0, to = 100) opacity: Int): Int {
    return context.colorWithOpacity(res, opacity)
}


/**
 * Get bitmap representation of view
 */
fun View.asBitmap(): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    layout(left, top, right, bottom)
    draw(c)
    return b
}


/**
 * View artificial attribute that sets compound left drawable
 */
var TextView.drawableLeft: Int
    get() = throw IllegalAccessException("Property drawableLeft only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(context.drawable(value), drawables[1], drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound right drawable
 */
var TextView.drawableRight: Int
    get() = throw IllegalAccessException("Property drawableRight only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], context.drawable(value), drawables[3])
    }

/**
 * View artificial attribute that sets compound top drawable
 */
var TextView.drawableTop: Int
    get() = throw IllegalAccessException("Property drawableTop only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], context.drawable(value), drawables[2], drawables[3])
    }

/**
 * View artificial attribute that sets compound bottom drawable
 */
var TextView.drawableBottom: Int
    get() = throw IllegalAccessException("Property drawableBottom only as setter")
    set(value) {
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], context.drawable(value))
    }


/**
 * Convert this Drawable to Bitmap representation. Should take care of every Drawable type
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    Canvas(bitmap).apply {
        setBounds(0, 0, width, height)
        draw(this)
    }
    return bitmap
}

fun View.setOnClickCoroutine(owner: LifecycleOwner, listener: suspend (view: View) -> Unit) =
        this.setOnClickListener { owner.lifecycleScope.launch { listener(it) } }


fun View.fakeTouch() {
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis() + 100
    val x = 0.0f
    val y = 0.0f
    val metaState = 0
    val motionEvent = MotionEvent.obtain(
            downTime,
            eventTime,
            MotionEvent.ACTION_UP,
            x,
            y,
            metaState
    )
    dispatchTouchEvent(motionEvent)
    motionEvent.recycle()
}


fun View.doOnApplyWindowInsets(f: (View, insets: WindowInsetsCompat, initialPadding: ViewDimensions, initialMargin: ViewDimensions) -> Unit
) {
    // Create a snapshot of the view's padding state
    val initialPadding = createStateForViewPadding(this)
    val initialMargin = createStateForViewMargin(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding, initialMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}


private fun createStateForViewPadding(view: View) = ViewDimensions(
        view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart,
        view.paddingEnd
)

private fun createStateForViewMargin(view: View): ViewDimensions {
    return (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
        ViewDimensions(it.leftMargin, it.topMargin, it.rightMargin, it.bottomMargin,
                it.marginStart, it.marginEnd)
    } ?: ViewDimensions()
}

data class ViewDimensions(
        val left: Int = 0,
        val top: Int = 0,
        val right: Int = 0,
        val bottom: Int = 0,
        val start: Int = 0,
        val end: Int = 0
)

fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

fun PopupMenu.hideItem(@IdRes res: Int, hide: Boolean = true) {
    menu.findItem(res).isVisible = !hide
}

fun android.widget.PopupMenu.hideItem(@IdRes res: Int, hide: Boolean = true) {
    menu.findItem(res).isVisible = !hide
}


@TargetApi(value = Build.VERSION_CODES.M)
fun View.resetForeground() {
    if (canUseForeground) {
        foreground = null
    }
}

/**
 * Reads the file attributes safely
 * @receiver View
 * @param attrs AttributeSet?
 * @param styleableArray IntArray
 * @param block [@kotlin.ExtensionFunctionType] Function1<TypedArray, Unit>
 */
inline fun View.readAttributes(
        attrs: AttributeSet?,
        styleableArray: IntArray,
        block: TypedArray.() -> Unit
) {
    val typedArray = context.theme.obtainStyledAttributes(attrs, styleableArray, 0, 0)
    typedArray.use {
        it.block()
    }  // From androidx.core
}


fun View.resetBackground() {
    setBackgroundResource(context.selectableItemBackgroundResource)
}


fun View.unsetRippleClickAnimation() =
        if (canUseForeground) resetForeground() else resetBackground()


private val canUseForeground
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


fun View.visibleIfTrueGoneOtherwise(condition: Boolean) {
    if (condition) {
        visible()
    } else {
        gone()
    }
}

fun View.goneIfTrueVisibleOtherwise(condition: () -> Boolean) {
    if (condition()) {
        gone()
    } else {
        visible()
    }
}

/**
 * Animates the view to rotate, can be refactored for more abstraction if needed
 *
 * @param rotation Value of rotation to be applied to view
 * @param duration Duration in millis of the rotation animation
 */
fun View.rotateAnimation(rotation: Float, duration: Long) {
    val interpolator = OvershootInterpolator()
    isActivated = if (!isActivated) {
        ViewCompat.animate(this).rotation(rotation).withLayer().setDuration(duration).setInterpolator(interpolator).start()
        !isActivated
    } else {
        ViewCompat.animate(this).rotation(0f).withLayer().setDuration(duration).setInterpolator(interpolator).start()
        !isActivated
    }
}

fun View.blink(duration: Long) {
    val anim = AlphaAnimation(0.3f, 1.0f)
    anim.duration = duration
    startAnimation(anim)
}

/**
 * Returns the innermost focused child within this [View] hierarchy, or null if this is not a [ViewGroup]
 */
val View.innermostFocusedChild: View?
    get() {
        if (this !is ViewGroup) return null
        val focused = focusedChild
        return focused?.innermostFocusedChild ?: focused
    }


fun View.visibilityChangeListener(onVisibility: (isVisible: Boolean) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener {
        onVisibility(isVisible)
    }
}


fun AppBarLayout.disableDragging() {
    val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
    val behavior = layoutParams.behavior as AppBarLayout.Behavior?
    if (behavior != null) {
        behavior.setDragCallback(DisabledDragCallback)
    } else {
        doOnLayout {
            (layoutParams.behavior as AppBarLayout.Behavior).setDragCallback(DisabledDragCallback)
        }
    }
}

fun AppBarLayout.invalidateScrollRanges() {
    invalidateScrollRangesMethod(this)
}

inline fun TabLayout.doOnTabReselected(crossinline action: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            action(tab)
        }
    })
}

inline fun TabLayout.doOnTabSelected(crossinline action: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            action(tab)

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    })
}


inline fun TabLayout.doOnTabUnSelected(crossinline action: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            action(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    })
}


inline fun <reified T : ViewParent> View.findParentOfType(): T? {
    return findParentOfType(T::class.java)
}

fun <T : ViewParent> View.findParentOfType(type: Class<T>): T? {
    var p = parent
    while (p != null) {
        if (type.isInstance(p)) {
            return type.cast(p)
        }
        p = p.parent
    }
    return null
}


fun Toolbar.setTitleOnClickListener(onClickListener: View.OnClickListener) {
    var titleView = toolbarTitleField.get(this) as View?
    if (titleView == null) {
        val title = title
        this.title = " " // Force Toolbar to create mTitleTextView
        this.title = title
        titleView = toolbarTitleField.get(this) as View
    }
    titleView.setOnClickListener(onClickListener)
}

fun Toolbar.setSubtitleOnClickListener(onClickListener: View.OnClickListener) {
    var subtitleView = toolbarSubtitleField.get(this) as View?
    if (subtitleView == null) {
        val subtitle = subtitle
        this.subtitle = " " // Force Toolbar to create mSubtitleTextView
        this.subtitle = subtitle
        subtitleView = toolbarSubtitleField.get(this) as View
    }
    subtitleView.setOnClickListener(onClickListener)
}

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.getLayoutBehavior(): T {
    val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
    return layoutParams.behavior as T
}

private val toolbarTitleField: Field by lazy(LazyThreadSafetyMode.NONE) {
    Toolbar::class.java.getDeclaredField("mTitleTextView").apply { isAccessible = true }
}

private val toolbarSubtitleField: Field by lazy(LazyThreadSafetyMode.NONE) {
    Toolbar::class.java.getDeclaredField("mSubtitleTextView").apply { isAccessible = true }
}


private object DisabledDragCallback : AppBarLayout.Behavior.DragCallback() {
    override fun canDrag(appBarLayout: AppBarLayout): Boolean = false
}

private val invalidateScrollRangesMethod: Method by lazy(LazyThreadSafetyMode.NONE) {
    AppBarLayout::class.java.getDeclaredMethod("invalidateScrollRanges").apply { isAccessible = true }
}

fun TabLayout.addMarginInTabLayout(color: Int, width: Int, height: Int, paddingFromDivider: Int) {
    val linearLayout = getChildAt(0) as LinearLayout
    linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.setSize(width, height)
    linearLayout.dividerPadding = paddingFromDivider
    linearLayout.dividerDrawable = drawable
}

fun View.forceScrollGestures() {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                // Disallow ScrollView to intercept touch events.
                v.parent.requestDisallowInterceptTouchEvent(true)

            MotionEvent.ACTION_UP ->
                // Allow ScrollView to intercept touch events.
                v.parent.requestDisallowInterceptTouchEvent(false)
        }

        v.onTouchEvent(event)
        true
    }
}



