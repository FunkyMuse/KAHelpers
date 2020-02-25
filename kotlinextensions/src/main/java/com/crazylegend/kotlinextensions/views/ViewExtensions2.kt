package com.crazylegend.kotlinextensions.views

import android.annotation.TargetApi
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.crazylegend.kotlinextensions.collections.use
import com.crazylegend.kotlinextensions.context.colorWithOpacity
import com.crazylegend.kotlinextensions.context.drawable
import com.crazylegend.kotlinextensions.context.inputManager
import com.crazylegend.kotlinextensions.context.selectableItemBackgroundResource
import kotlinx.coroutines.launch


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


fun View.visibleIfTrueGoneOtherwise(condition:Boolean){
    if (condition){
        visible()
    } else {
        gone()
    }
}

fun View.visibleIfTrueGoneOtherwise(condition:()->Boolean){
    if (condition()){
        visible()
    } else {
        gone()
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
