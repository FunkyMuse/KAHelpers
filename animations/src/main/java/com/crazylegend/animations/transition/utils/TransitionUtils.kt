package com.crazylegend.animations.transition.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.crazylegend.animations.transition.interpolators.FAST_OUT_SLOW_IN
import com.crazylegend.animations.transition.onTransitionEnd


/**
 * Created by crazy on 3/27/20 to long live and prosper !
 */

/**
 * Usage
TransitionManager.beginDelayedTransition(
card,
fadeThrough.setDuration(MEDIUM_EXPAND_DURATION)
)
 */
fun fadeThrough(): Transition {
    return transitionTogether {
        interpolator = FAST_OUT_SLOW_IN
        this += ChangeBounds()
        this += transitionSequential {
            addTransition(Fade(Fade.OUT))
            addTransition(Fade(Fade.IN))
        }
    }
}

inline fun transitionTogether(crossinline body: TransitionSet.() -> Unit): TransitionSet {
    return TransitionSet().apply {
        ordering = TransitionSet.ORDERING_TOGETHER
        body()
    }
}

inline fun transitionSequential(
        crossinline body: SequentialTransitionSet.() -> Unit
): SequentialTransitionSet {
    return SequentialTransitionSet().apply(body)
}

inline fun TransitionSet.forEach(action: (transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

inline fun TransitionSet.forEachIndexed(action: (index: Int, transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(i, getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

operator fun TransitionSet.iterator() = object : MutableIterator<Transition> {

    private var index = 0

    override fun hasNext() = index < transitionCount

    override fun next() =
            getTransitionAt(index++) ?: throw IndexOutOfBoundsException()

    override fun remove() {
        removeTransition(getTransitionAt(--index) ?: throw IndexOutOfBoundsException())
    }
}

operator fun TransitionSet.plusAssign(transition: Transition) {
    addTransition(transition)
}

operator fun TransitionSet.get(i: Int): Transition {
    return getTransitionAt(i) ?: throw IndexOutOfBoundsException()
}

fun fadeRecyclerTransition(recycler: RecyclerView, savedItemAnimator: RecyclerView.ItemAnimator?): SequentialTransitionSet {
    return transitionSequential {
        duration = LARGE_EXPAND_DURATION
        interpolator = FAST_OUT_SLOW_IN
        this += Fade(Fade.OUT)
        this += Fade(Fade.IN)
        onTransitionEnd {
            if (savedItemAnimator != null) {
                recycler.itemAnimator = savedItemAnimator
            }
        }
    }
}