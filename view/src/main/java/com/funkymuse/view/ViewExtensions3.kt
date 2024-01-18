package com.funkymuse.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.animation.ArgbEvaluatorCompat
import kotlin.math.max


inline fun View.ifVisible(action: () -> Unit) {
    if (isVisible) action()
}


inline fun View.ifInvisible(action: () -> Unit) {
    if (isInvisible) action()
}


inline fun View.ifGone(action: () -> Unit) {
    if (isGone) action()
}


private val tmpIntArr = IntArray(2)

/**
 * Function which updates the given [rect] with this view's position and bounds in its window.
 */
fun View.copyBoundsInWindow(rect: Rect) {
    if (isLaidOut && isAttachedToWindow) {
        rect.set(0, 0, width, height)
        getLocationInWindow(tmpIntArr)
        rect.offset(tmpIntArr[0], tmpIntArr[1])
    } else {
        throw IllegalArgumentException("Can not copy bounds as view is not laid out" +
                " or attached to window")
    }
}


/**
 * Hides all the views passed in the arguments
 */
fun hideViews(vararg views: View) = views.asSequence().forEach { it.visibility = View.GONE }

/**
 * Shows all the views passed in the arguments
 */
fun showViews(vararg views: View) = views.asSequence().forEach { it.visibility = View.VISIBLE }




val View.isAppearanceLightNavigationBars
    get() =
        ViewCompat.getWindowInsetsController(this)?.isAppearanceLightNavigationBars

val View.isAppearanceLightStatusBars
    get() =
        ViewCompat.getWindowInsetsController(this)?.isAppearanceLightStatusBars

fun View.createCircularReveal(
        revealDuration: Long = 1500L,
        centerX: Int = 0,
        centerY: Int = 0,
        @ColorRes startColor: Int,
        @ColorRes endColor: Int,
        showAtEnd: Boolean = true): Animator {

    val radius = max(width, height).toFloat()
    val startRadius = if (showAtEnd) 0f else radius
    val finalRadius = if (showAtEnd) radius else 0f

    val animator =
            ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, finalRadius).apply {
                interpolator = FastOutSlowInInterpolator()
                duration = revealDuration
                doOnEnd {
                    isVisible = showAtEnd
                }
                start()
            }


    ValueAnimator().apply {
        setIntValues(ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor))
        setEvaluator(ArgbEvaluatorCompat())
        addUpdateListener { valueAnimator -> setBackgroundColor((valueAnimator.animatedValue as Int)) }
        duration = revealDuration

        start()
    }
    return animator
}

fun View.accessibleTouchTarget() {
    post {
        val delegateArea = Rect()
        getHitRect(delegateArea)

        // 48 dp is the minimum requirement. We need to convert this to pixels.
        val accessibilityMin = context.dpToPx(48)

        // Calculate size vertically, and adjust touch area if it's smaller then the minimum.
        val height = delegateArea.bottom - delegateArea.top
        if (accessibilityMin > height) {
            // Add +1 px just in case min - height is odd and will be rounded down
            val addition = ((accessibilityMin - height) / 2).toInt() + 1
            delegateArea.top -= addition
            delegateArea.bottom += addition
        }

        // Calculate size horizontally, and adjust touch area if it's smaller then the minimum.
        val width = delegateArea.right - delegateArea.left
        if (accessibilityMin > width) {
            // Add +1 px just in case min - width is odd and will be rounded down
            val addition = ((accessibilityMin - width) / 2).toInt() + 1
            delegateArea.left -= addition
            delegateArea.right += addition
        }

        val parentView = parent as? View
        parentView?.touchDelegate = TouchDelegate(delegateArea, this)
    }
}

fun Context.dpToPx(value: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics)