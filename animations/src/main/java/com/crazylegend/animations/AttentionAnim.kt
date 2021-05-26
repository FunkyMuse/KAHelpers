package com.crazylegend.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.crazylegend.animations.*

/**
 * Created by crazy on 10/6/19 to long live and prosper !
 */

fun View.attentionBounce(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationY_CONST, 0f, 0f, -30f, 0f, -15f, 0f, 0f)

    animatorSet.playTogether(object1)
    return animatorSet
}

fun View.attentionFlash(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_alpha_CONST, 1f, 0f, 1f, 0f, 1f)

    animatorSet.playTogether(object1)
    return animatorSet
}

fun View.attentionPulse(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 1.1f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 1.1f, 1f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.attentionRuberband(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 1.25f, 0.75f, 1.15f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.75f, 1.25f, 0.85f, 1f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}

fun View.attentionShake(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0.toFloat(), 25.toFloat(),
            (-25).toFloat(), 25.toFloat(),
            (-25).toFloat(), 15.toFloat(),
            (-15).toFloat(), 6.toFloat(),
            (-6).toFloat(), 0.toFloat())

    animatorSet.play(object1)
    return animatorSet
}

fun View.attentionStandup(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = ((width - paddingLeft - paddingRight) / 2 + paddingLeft).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x, x, x, x)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y, y, y, y)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotationX_CONST, 55f, -30f, 15f, -15f, 0f)

    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.attentionSwing(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)

    animatorSet.playTogether(object1)
    return animatorSet
}

fun View.attentionTada(): AnimatorSet {
    val animatorSet = AnimatorSet()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleX_CONST, 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_scaleY_CONST, 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, -3f, -3f, 3f, -3f, 3f, -3f, 3f, -3f, 0f)


    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.attentionWave(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val x = ((width - paddingLeft - paddingRight) / 2 + paddingLeft).toFloat()
    val y = (height - paddingBottom).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 12f, -12f, 3f, -3f, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotX_CONST, x, x, x, x, x)
    val object3: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_pivotY_CONST, y, y, y, y, y)

    animatorSet.playTogether(object1, object2, object3)
    return animatorSet
}

fun View.attentionWobble(): AnimatorSet {
    val animatorSet = AnimatorSet()
    val width = (this.width).toFloat()
    val one = (width / 100.0).toFloat()

    val object1: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_translationX_CONST, 0f * one, -25f * one, 20f * one, -15f * one, 10f * one, -5f * one, 0f * one, 0f)
    val object2: ObjectAnimator = ObjectAnimator.ofFloat(this, ANIM_rotation_CONST, 0f, -5f, 3f, -3f, 2f, -1f, 0f)

    animatorSet.playTogether(object1, object2)
    return animatorSet
}