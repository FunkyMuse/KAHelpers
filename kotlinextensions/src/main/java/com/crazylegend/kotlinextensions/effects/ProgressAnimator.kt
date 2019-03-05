package com.crazylegend.kotlinextensions.effects

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.Interpolator


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */
class ProgressAnimator private constructor(private vararg val values: Float) {

    companion object {
        inline fun ofFloat(crossinline builder: ProgressAnimator.() -> Unit) = ofFloat(0f, 1f) { builder() }

        fun ofFloat(vararg values: Float, builder: ProgressAnimator.() -> Unit) = ProgressAnimator(*values).apply {
            builder()
            build()
        }
    }

    private val animators: MutableList<(Float) -> Unit> = mutableListOf()
    private val startActions: MutableList<() -> Unit> = mutableListOf()
    private val endActions: MutableList<() -> Unit> = mutableListOf()

    var duration: Long = -1L
    var interpolator: Interpolator? = null

    /**
     * Add more changes to the [ValueAnimator] before running
     */
    var extraConfigs: ValueAnimator.() -> Unit = {}

    /**
     * Range animator. Multiples the range by the current float progress before emission
     */
    fun withAnimator(from: Float, to: Float, animator: (Float) -> Unit) = animators.add {
        val range = to - from
        animator(range * it + from)
    }

    /**
     * Standard animator. Emits progress value as is
     */
    fun withAnimator(animator: (Float) -> Unit) = animators.add(animator)

    /**
     * Start action to be called once when the animator first begins
     */
    fun withStartAction(action: () -> Unit) = startActions.add(action)

    /**
     * End action to be called once when the animator ends
     */
    fun withEndAction(action: () -> Unit) = endActions.add(action)

    fun build() {
        ValueAnimator.ofFloat(*values).apply {
            if (this@ProgressAnimator.duration > 0L)
                duration = this@ProgressAnimator.duration
            if (this@ProgressAnimator.interpolator != null)
                interpolator = this@ProgressAnimator.interpolator
            addUpdateListener {
                val progress = it.animatedValue as Float
                animators.forEach { it(progress) }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    startActions.forEach { it() }
                }

                override fun onAnimationEnd(animation: Animator?) {
                    endActions.forEach { it() }
                }

                override fun onAnimationCancel(animation: Animator?) {
                    endActions.forEach { it() }
                }
            })
            extraConfigs()
            start()
        }
    }
}