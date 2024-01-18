package com.funkymuse.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


fun View.flipInX(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0.25f, 0.5f, 0.75f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotationX_CONST, 90f, -15f, 15f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.flipInY(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0.25f, 0.5f, 0.75f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotationY_CONST, 90f, -15f, 15f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.flipOutX(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotationX_CONST, 0f, 90f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.flipOutY(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotationY_CONST, 0f, 90f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}
