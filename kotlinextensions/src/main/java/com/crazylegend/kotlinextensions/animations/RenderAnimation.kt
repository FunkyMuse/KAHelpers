package com.crazylegend.kotlinextensions.animations

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.view.animation.AccelerateInterpolator

/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */

fun AnimatorSet.playAnimation(animationDuration: Long = 1000L, customInterpolator: TimeInterpolator = AccelerateInterpolator()): AnimatorSet {
    interpolator = customInterpolator
    duration = animationDuration
    start()
    return this
}