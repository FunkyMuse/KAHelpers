package com.crazylegend.kotlinextensions.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.crazylegend.kotlinextensions.context.dp2px
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.context.px2dp
import com.google.android.material.animation.ArgbEvaluatorCompat
import kotlin.math.max


/**
 * Created by crazy on 7/24/20 to long live and prosper !
 */

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

fun View.px2dp(pxValue: Float): Float? {
    return context?.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int? {
    return context?.dp2px(dpValue)
}

fun View.dp2px(dpValue: Int): Int? {
    return context?.dp2px(dpValue)
}

fun View.px2dp(pxValue: Int): Float? {
    return context?.px2dp(pxValue)
}


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
        setIntValues(context.getCompatColor(startColor), context.getCompatColor(endColor))
        setEvaluator(ArgbEvaluatorCompat())
        addUpdateListener { valueAnimator -> setBackgroundColor((valueAnimator.animatedValue as Int)) }
        duration = revealDuration

        start()
    }
    return animator
}