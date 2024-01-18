package com.funkymuse.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup


fun View.slideInDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val distance = (top + height).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, -distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, -distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideInUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, distance, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, 0f, distance)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val right = right.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0f, -right)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - left).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0f, distance)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.slideOutUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val bottom = -bottom.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, 0f, bottom)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}



