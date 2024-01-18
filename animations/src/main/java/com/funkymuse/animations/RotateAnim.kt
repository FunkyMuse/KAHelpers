package com.funkymuse.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


fun View.rotateIn(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, -200f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.rotateInDownLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, -90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInDownRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInUpLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = height - paddingBottom.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInUpRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, -90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

/*
out
 */

fun View.rotateOut(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, 200f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.rotateOutDownLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, 90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}


fun View.rotateOutDownRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, -90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateOutUpLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (paddingLeft).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, -90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateOutUpRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, 90f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}
