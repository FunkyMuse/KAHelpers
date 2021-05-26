package com.crazylegend.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.CycleInterpolator
import androidx.annotation.ColorRes
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.crazylegend.view.dp
import kotlin.math.hypot

/**
 * Created by crazy on 1/16/20 to long live and prosper !
 */
object AnimationConstants {
    const val LICKETY_SPLIT = 150L
    const val SHORT = 250L
    const val MEDIUM = 400L
    const val LONG = 600L

    val TRANSLATION_SMALL = 16.dp
    val TRANSLATION_MICRO = 4.dp

    val LINEAR_OUT_SLOW_IN by lazy { LinearOutSlowInInterpolator() }
    val FAST_OUT_SLOW_IN by lazy { FastOutSlowInInterpolator() }
    val FAST_OUT_LINEAR_IN by lazy { FastOutLinearInInterpolator() }
    val CYCLE_2 by lazy { CycleInterpolator(2f) }

}


fun View.fade(initAlpha: Float? = null, alpha: Float = 1f, startDelay: Long = 0,
              build: (ViewPropertyAnimator.() -> Unit)? = null) {
    initAlpha?.let { this.alpha = initAlpha }
    val anim = animate()
            .alpha(alpha)
            .setStartDelay(startDelay)
            .setDuration(AnimationConstants.SHORT)
            .setInterpolator(AnimationConstants.LINEAR_OUT_SLOW_IN)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (alpha == 0f) visibility = View.INVISIBLE
                }
            })
    build?.let { anim.build() }
    anim.start()
}

fun View.translate(initX: Int? = null, initY: Int? = null, translationX: Int = 0, translationY: Int = 0,
                   initAlpha: Float? = null, alpha: Float = 1f, startDelay: Long = 0,
                   build: (ViewPropertyAnimator.() -> Unit)? = null) {
    initX?.let { this.translationX = initX.toFloat() }
    initY?.let { this.translationY = initY.toFloat() }
    initAlpha?.let { this.alpha = initAlpha }
    val anim = animate()
            .alpha(alpha)
            .translationX(translationX.toFloat())
            .translationY(translationY.toFloat())
            .setStartDelay(startDelay)
            .setDuration(AnimationConstants.MEDIUM)
            .setInterpolator(AnimationConstants.FAST_OUT_SLOW_IN)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (alpha == 0f) visibility = View.INVISIBLE
                }
            })
    build?.let { anim.build() }
    anim.start()
}

fun View.fadeInVertical(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = translate(initY = AnimationConstants.TRANSLATION_SMALL, translationY = 0, alpha = 1f, startDelay = startDelay) {
    duration = AnimationConstants.MEDIUM
    interpolator = AnimationConstants.LINEAR_OUT_SLOW_IN
    build?.invoke(this)
}

fun View.fadeOutVertical(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = translate(translationY = -AnimationConstants.TRANSLATION_SMALL, alpha = 0f, startDelay = startDelay) {
    duration = AnimationConstants.SHORT
    interpolator = AnimationConstants.FAST_OUT_SLOW_IN
    setListener(null)
    build?.invoke(this)
}

fun View.fadeInHorizontal(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = translate(initX = AnimationConstants.TRANSLATION_SMALL, translationX = 0, alpha = 1f, startDelay = startDelay) {
    duration = AnimationConstants.MEDIUM
    interpolator = AnimationConstants.LINEAR_OUT_SLOW_IN
    build?.invoke(this)
}

fun View.fadeOutHorizontal(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = translate(translationX = -AnimationConstants.TRANSLATION_SMALL, alpha = 0f, startDelay = startDelay) {
    duration = AnimationConstants.SHORT
    interpolator = AnimationConstants.FAST_OUT_SLOW_IN
    setListener(null)
    build?.invoke(this)
}

fun easeViewsInVertical(startDelay: Long = 0, vararg views: View?, build: (ViewPropertyAnimator.() -> Unit)? = null) {
    var offset = AnimationConstants.TRANSLATION_SMALL.toFloat()
    views.filterNotNull().filter { it.visibility != View.VISIBLE }.forEach {
        it.alpha = 0.8f
        it.translationY = offset
        val anim = it.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(startDelay)
                .setDuration(AnimationConstants.LONG)
                .setInterpolator(AnimationConstants.FAST_OUT_SLOW_IN)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        it.visibility = View.VISIBLE
                    }
                })
        build?.let { anim.build() }
        anim.start()
        offset *= 1.5f
    }
}

fun View.easeInVertical(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = easeViewsInVertical(startDelay, this) { build?.let { build() } }

fun easeViewsInHorizontal(startDelay: Long = 0, vararg views: View?, build: (ViewPropertyAnimator.() -> Unit)? = null) {
    var offset = AnimationConstants.TRANSLATION_SMALL.toFloat()
    views.filterNotNull().filter { it.visibility != View.VISIBLE }.forEach {
        it.alpha = 0.8f
        it.translationX = offset
        val anim = it.animate()
                .alpha(1f)
                .translationX(0f)
                .setStartDelay(startDelay)
                .setDuration(AnimationConstants.LONG)
                .setInterpolator(AnimationConstants.FAST_OUT_SLOW_IN)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        it.visibility = View.VISIBLE
                    }
                })
        build?.let { anim.build() }
        anim.start()
        offset *= 1.5f
    }
}

fun View.easeInHorizontal(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = easeViewsInHorizontal(startDelay, this) { build?.let { build() } }

fun easeViewsOutVertical(startDelay: Long = 0, vararg views: View?, build: (ViewPropertyAnimator.() -> Unit)? = null) {
    var offset = AnimationConstants.TRANSLATION_SMALL.toFloat()
    views.filterNotNull().filter { it.visibility != View.VISIBLE }.forEach {
        val anim = it.animate()
                .alpha(0f)
                .translationY(-offset)
                .setStartDelay(startDelay)
                .setDuration(AnimationConstants.LONG)
                .setInterpolator(AnimationConstants.FAST_OUT_SLOW_IN)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        it.visibility = View.INVISIBLE
                    }
                })
        build?.let { anim.build() }
        anim.start()
        offset *= 1.5f
    }
}

fun View.easeOutVertical(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = easeViewsOutVertical(startDelay, this) { build?.let { build() } }

fun easeViewsOutHorizontal(startDelay: Long = 0, vararg views: View?, build: (ViewPropertyAnimator.() -> Unit)? = null) {
    var offset = AnimationConstants.TRANSLATION_SMALL.toFloat()
    views.filterNotNull().filter { it.visibility != View.VISIBLE }.forEach {
        val anim = it.animate()
                .alpha(0f)
                .translationX(-offset)
                .setStartDelay(startDelay)
                .setDuration(AnimationConstants.LONG)
                .setInterpolator(AnimationConstants.FAST_OUT_SLOW_IN)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        it.visibility = View.INVISIBLE
                    }
                })
        build?.let { anim.build() }
        anim.start()
        offset *= 1.5f
    }
}

fun View.easeOutHorizontal(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = easeViewsOutHorizontal(startDelay, this) { build?.let { build() } }

fun View.reveal(show: Boolean = true, centerX: Int? = null, centerY: Int? = null,
                startRadius: Int? = null, endRadius: Int? = null,
                build: (Animator.() -> Unit)? = null) {
    if (!isAttachedToWindow) {
        visibility = if (show) View.VISIBLE else View.INVISIBLE
        return
    }
    val cx = (left + right) / 2
    val cy = (top + bottom) / 2
    val radius = hypot(width.toDouble(), height.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this,
            centerX ?: cx, centerY ?: cy,
            startRadius?.toFloat() ?: if (show) 0f else radius, endRadius?.toFloat()
            ?: if (show) radius else 0f
    )
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            if (show) {
                visibility = View.VISIBLE
            }
        }

        override fun onAnimationEnd(animation: Animator?) {
            if (!show) {
                visibility = View.INVISIBLE
            }
        }
    })
    build?.let { anim.build() }
    anim.start()
}

fun View.hide(centerX: Int? = null, centerY: Int? = null, build: (Animator.() -> Unit)? = null) = reveal(false, centerX, centerY, null, null, build)

fun View.rotate(initRotation: Int? = null, rotation: Int = 0,
                initAlpha: Float? = null, alpha: Float = 1f, startDelay: Long = 0,
                build: (ViewPropertyAnimator.() -> Unit)? = null) {
    initRotation?.let { this.rotation = initRotation.toFloat() }
    initAlpha?.let { this.alpha = initAlpha }
    if (alpha == 1f) visibility = View.VISIBLE
    val anim = animate()
            .alpha(alpha)
            .rotation(rotation.toFloat())
            .setStartDelay(startDelay)
            .setDuration(AnimationConstants.MEDIUM)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (alpha == 0f) visibility = View.INVISIBLE
                }
            })
    build?.let { anim.build() }
    anim.start()
}

fun View.rotateIn(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = rotate(initRotation = -180, initAlpha = 0f, startDelay = startDelay, build = build)

fun View.rotateOut(startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) = rotate(rotation = 180, alpha = 0f, startDelay = startDelay, build = build)

fun View.morphTo(target: View, startDelay: Long = 0, build: (ViewPropertyAnimator.() -> Unit)? = null) {
    this.rotateOut(startDelay, build)
    target.rotateIn(startDelay, build)
}

fun View.shake(build: (ViewPropertyAnimator.() -> Unit)? = null) {
    if (translationX != 0f) return
    val anim = animate()
            .translationX(AnimationConstants.TRANSLATION_MICRO.toFloat())
            .setDuration(AnimationConstants.LONG)
            .setInterpolator(AnimationConstants.CYCLE_2)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    translationX = 0f
                }
            })
    build?.let { anim.build() }
    anim.start()
}

fun colorFade(@ColorRes from: Int, @ColorRes to: Int, build: (ValueAnimator.() -> Unit)? = null, update: (Int) -> Unit) {
    val anim = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
    anim.duration = AnimationConstants.MEDIUM
    anim.interpolator = AnimationConstants.LINEAR_OUT_SLOW_IN
    anim.addUpdateListener { update(it.animatedValue as Int) }
    build?.let { anim.build() }
    anim.start()
}

fun View.colorFade(@ColorRes from: Int? = null, @ColorRes to: Int, build: (ValueAnimator.() -> Unit)? = null) {
    if (background !is ColorDrawable) {
        throw IllegalArgumentException("View needs to have a ColorDrawable background in order to animate the color")
    }
    val fromColor = from ?: (background as ColorDrawable).color
    colorFade(fromColor, to, build) { setBackgroundColor(it) }
}

fun ViewGroup.transition(transition: Transition? = null) {
    if (transition != null) {
        TransitionManager.beginDelayedTransition(this, transition)
    } else {
        TransitionManager.beginDelayedTransition(this)
    }
}
