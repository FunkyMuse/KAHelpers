package dev.funkymuse.animations

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.view.animation.AccelerateInterpolator




const val ANIM_translationY_CONST = "translationY"
const val ANIM_translationX_CONST = "translationX"
const val ANIM_alpha_CONST = "alpha"
const val ANIM_scaleY_CONST = "scaleY"
const val ANIM_scaleX_CONST = "scaleX"
const val ANIM_pivotX_CONST = "pivotX"
const val ANIM_pivotY_CONST = "pivotY"
const val ANIM_rotationX_CONST = "rotationX"
const val ANIM_rotationY_CONST = "rotationY"
const val ANIM_rotation_CONST = "rotation"


fun AnimatorSet.playAnimation(animationDuration: Long = 1000L, customInterpolator: TimeInterpolator = AccelerateInterpolator()): AnimatorSet {
    interpolator = customInterpolator
    duration = animationDuration
    start()
    return this
}