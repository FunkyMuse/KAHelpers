package com.crazylegend.kotlinextensions.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */

fun View.bounceIn(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1.05f, 0.9f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1.05f, 0.9f, 1f)

    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.bounceInLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = -width.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", width, 30f, -10f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)


    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.bounceInRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = -width.toFloat()
    val measured_width = -measuredWidth.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", measured_width + width, -30f, 10f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.bounceInUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val measured_height = measuredHeight.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", measured_height, -30f, 10f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.bounceInDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val height = -height.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", height, 30f, -10f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}