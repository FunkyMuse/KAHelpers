package com.crazylegend.kotlinextensions.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */


fun View.rotateIn(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", -200f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.rotateInDownLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", -90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInDownRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInUpLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = height - paddingBottom.toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateInUpRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", -90f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

/*
out
 */

fun View.rotateOut(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 200f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.rotateOutDownLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = paddingLeft.toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}


fun View.rotateOutDownRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, -90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateOutUpLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (paddingLeft).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, -90f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4: ObjectAnimator = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.rotateOutUpRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = (width - paddingRight).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1 = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, "rotation", 0f, 90f)
    val object3 = ObjectAnimator.ofFloat(this, "pivotX", x, x)
    val object4 = ObjectAnimator.ofFloat(this, "pivotY", y, y)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}
