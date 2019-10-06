package com.crazylegend.kotlinextensions.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup


/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */

fun View.slideInDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val distance = (top + height).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", -distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", -distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, distance)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val right = right.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0f, -right)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0f, distance)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val bottom = -bottom.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, bottom)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}



