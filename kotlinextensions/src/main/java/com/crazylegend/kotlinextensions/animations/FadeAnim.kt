package com.crazylegend.kotlinextensions.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */

fun View.fadeIn(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)

    animatorSet.playTogether(object1)
    return animatorSet
}

fun View.fadeInLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = -width.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", width / 4f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeInRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = width.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", width / 4f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeInUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val height = height.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", height / 4f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeInDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val height = -height.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", height / 4f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

/*
Out
 */

fun View.fadeOut(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)

    animatorSet.playTogether(object1)
    return animatorSet
}

fun View.fadeOutLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = -width.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0f, width / 4f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeOutRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = width.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", width / 4f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeOutUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val height = height.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, height / 4f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.fadeOutDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val height = -height.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, height / 4f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

