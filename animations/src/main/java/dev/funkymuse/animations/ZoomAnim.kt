package dev.funkymuse.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup



fun View.zoomIn(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 0.45f, 1f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 0.45f, 1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f)

    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.zoomInDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val bottom = -bottom.toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 0.1f, 0.475f, 1f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 0.1f, 0.475f, 1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, bottom, 60f, 0f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f, 1f)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.zoomInLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val right = -right.toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 0.1f, 0.475f, 1f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 0.1f, 0.475f, 1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, right, 48f, 0f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f, 1f)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.zoomInRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = -width.toFloat()
    val right = -paddingRight.toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 0.1f, 0.475f, 1f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 0.1f, 0.475f, 1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, width + right, -48f, 0f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f, 1f)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.zoomInUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 0f, 1f, 1f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 0.1f, 0.475f, 1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 0.1f, 0.475f, 1f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, distance, -60f, 0f)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

/*
Out
 */

fun View.zoomOut(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.3f, 0f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.3f, 0f)

    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.zoomOutDown(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.height - top).toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.475f, 0.1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.475f, 0.1f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, 0f, -60f, distance)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}


fun View.zoomOutLeft(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val right = -right.toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.475f, 0.1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.475f, 0.1f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0f, 42f, right)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.zoomOutRight(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val parent = parent as ViewGroup
    val distance = (parent.width - parent.left).toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.475f, 0.1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.475f, 0.1f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0f, -42f, distance)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}

fun View.zoomOutUp(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val bottom = -bottom.toFloat()

    val object1 = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 1f, 0f)
    val object2 = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.475f, 0.1f)
    val object3 = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.475f, 0.1f)
    val object4 = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, 0f, 60f, bottom)

    animatorSet.playTogether(object1, object2, object3, object4)
    return animatorSet
}


