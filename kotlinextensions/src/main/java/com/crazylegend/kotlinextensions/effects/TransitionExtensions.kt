package com.crazylegend.kotlinextensions.effects

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.TransitionRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.crazylegend.kotlinextensions.views.scaleXY


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

fun Context.transition(enterTransition: Int, exitTransition: Int) {
    (this as AppCompatActivity).overridePendingTransition(enterTransition, exitTransition)
}

fun Activity.animate(intent: Intent, transitionImage: View, EXTRA_IMAGE: String) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionImage, EXTRA_IMAGE)
    ActivityCompat.startActivity(this, intent, options.toBundle())
}


/**
 * Animate a transition a given imageview
 * If it is not shown, the action will be invoked directly and no further actions will be made
 * If it is already shown, scaling and alpha animations will be added to the action
 */
inline fun <T : ImageView> T.fadeScaleTransition(
        duration: Long = 500L,
        minScale: Float = 0.7f,
        crossinline action: T.() -> Unit
) {
    if (!isVisible) action()
    else {
        var transitioned = false
        ValueAnimator.ofFloat(1.0f, 0.0f, 1.0f).apply {
            this.duration = duration
            addUpdateListener {
                val x = it.animatedValue as Float
                scaleXY = x * (1 - minScale) + minScale
                imageAlpha = (x * 255).toInt()
                if (it.animatedFraction > 0.5f && !transitioned) {
                    transitioned = true
                    action()
                }
            }
            start()
        }
    }
}


inline fun Transition.addListener(
        crossinline onTransitionEnd: (Transition) -> Unit = {},
        crossinline onTransitionResume: (Transition) -> Unit = {},
        crossinline onTransitionPause: (Transition) -> Unit = {},
        crossinline onTransitionCancel: (Transition) -> Unit = {},
        crossinline onTransitionStart: (Transition) -> Unit = {}
) {
    addListener(object : Transition.TransitionListener {
        override fun onTransitionEnd(transition: Transition) {
            onTransitionEnd(transition)
        }

        override fun onTransitionResume(transition: Transition) {
            onTransitionResume(transition)
        }

        override fun onTransitionPause(transition: Transition) {
            onTransitionPause(transition)
        }

        override fun onTransitionCancel(transition: Transition) {
            onTransitionCancel(transition)
        }

        override fun onTransitionStart(transition: Transition) {
            onTransitionStart(transition)
        }

    })
}

inline fun Transition.onTransitionEnd(crossinline onTransitionEnd: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionEnd = onTransitionEnd)
}

inline fun Transition.onTransitionResume(crossinline onTransitionResume: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionResume = onTransitionResume)
}

inline fun Transition.onTransitionPause(crossinline onTransitionPause: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionPause = onTransitionPause)
}

inline fun Transition.onTransitionCancel(crossinline onTransitionCancel: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionCancel = onTransitionCancel)
}

inline fun Transition.onTransitionStart(crossinline onTransitionStart: (Transition) -> Unit = { _ -> }) {
    addListener(onTransitionStart = onTransitionStart)
}

class TransitionEndListener(val onEnd: (transition: Transition) -> Unit) : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) = onEnd(transition)
    override fun onTransitionResume(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionStart(transition: Transition) {}
}


fun Transition.addEndListener(onEnd: (transition: Transition) -> Unit) {
    addListener(TransitionEndListener(onEnd))
}


inline fun ViewGroup.transitionAuto(builder: AutoTransition.() -> Unit = {}) {
    val transition = AutoTransition()
    transition.builder()
    TransitionManager.beginDelayedTransition(this, transition)
}

inline fun ViewGroup.transitionDelayed(@TransitionRes id: Int, builder: Transition.() -> Unit = {}) {
    val transition = TransitionInflater.from(context).inflateTransition(id)
    transition.builder()
    TransitionManager.beginDelayedTransition(this, transition)
}