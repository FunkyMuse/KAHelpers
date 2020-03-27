package com.crazylegend.kotlinextensions.transition.utils

import androidx.annotation.IdRes
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Transition
import com.crazylegend.kotlinextensions.transition.SharedFadeTransition
import com.crazylegend.kotlinextensions.transition.interpolators.FAST_OUT_SLOW_IN


/**
 * Created by crazy on 3/27/20 to long live and prosper !
 */

/**
 * Inside your [Fragment]
 * sharedElementEnterTransition = createSharedElementTransition(LARGE_EXPAND_DURATION)
sharedElementReturnTransition = createSharedElementTransition(LARGE_COLLAPSE_DURATION)
 */
fun createSharedElementTransitionFadeChangeBoundsTransform(duration: Long, @IdRes noTransform: Int): Transition {
    return transitionTogether {
        this.duration = duration
        interpolator = FAST_OUT_SLOW_IN
        this += SharedFadeTransition()
        this += ChangeBounds()
        this += ChangeTransform()
                // The content is already transformed along with the parent. Exclude it.
                .excludeTarget(noTransform, true)
    }
}