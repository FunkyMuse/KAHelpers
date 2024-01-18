package com.funkymuse.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.funkymuse.setofusefulkotlinextensions.animations.R
import com.funkymuse.view.invisible
import com.funkymuse.view.visible
import kotlin.math.hypot
import kotlin.math.max


fun slideLeftRight(toHideView: View, fromView: View, toShowView: View, switchViews: Boolean) {

    if (switchViews) {
        if (toShowView.visibility == View.GONE) {
            toShowView.visibility = View.VISIBLE
        }
        val params =
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        params.addRule(RelativeLayout.LEFT_OF, toShowView.id)
        val marginLayoutParams = fromView.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(0, 0, -20, 0)
        params.setMargins(0, 0, -70, 0)
        fromView.layoutParams = params

        fromView.bringToFront()
        val translateAnimation = TranslateAnimation(0f, (-fromView.width / 2).toFloat(), 0f, 0f)
        translateAnimation.duration = 1000
        translateAnimation.fillAfter = true
        toHideView.startAnimation(translateAnimation)

        val translateAnimationRight =
            TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
        translateAnimationRight.duration = 1000
        translateAnimationRight.fillAfter = true
        toShowView.startAnimation(translateAnimationRight)
    } else {
        val params =
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        params.addRule(RelativeLayout.LEFT_OF, toHideView.id)
        val marginLayoutParams = fromView.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(0, 0, -20, 0)
        params.setMargins(0, 0, -70, 0)
        fromView.bringToFront()
        val translateAnimation = TranslateAnimation(0f, (-fromView.width / 2).toFloat(), 0f, 0f)
        translateAnimation.duration = 1000
        translateAnimation.fillAfter = true
        toShowView.startAnimation(translateAnimation)

        // toShowView.setVisibility(View.GONE);

        val translateAnimationRight =
            TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
        translateAnimationRight.duration = 1000
        translateAnimationRight.fillAfter = true
        toHideView.startAnimation(translateAnimationRight)
    }
}


fun View.slideUp() {

    val valueAnimator = ValueAnimator.ofInt(this.height, 0)
    valueAnimator.addUpdateListener { animation ->
        this.layoutParams.height = animation.animatedValue as Int
        this.requestLayout()
    }

    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 500
    valueAnimator.start()
}


fun Context.slideDown(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down))
}

fun Context.slideUp(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
}


fun View.slideDown(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
}

fun View.slideUp(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}


fun View.slideDown(second: View) {

    val valueAnimator = ValueAnimator.ofInt(0, second.height)
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.addUpdateListener { animation ->
        this.layoutParams.height = animation.animatedValue as Int
        this.requestLayout()
    }

    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 500
    valueAnimator.start()
}


fun Context.leftToRight(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_to_right))
}

fun View.animate(context: Context, animation: Int) {
    this.startAnimation(AnimationUtils.loadAnimation(context, animation))
}

fun Context.animate(view: View, animation: Int) {
    view.startAnimation(AnimationUtils.loadAnimation(this, animation))
}

fun Context.rightToLeft(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_to_left))
}


fun View.leftToRight(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right))
}

fun View.rightToLeft(context: Context) {
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_to_left))
}


fun View.circularReveal(
    x: Int = 0,
    y: Int = 0,
    offset: Long = 0L,
    radius: Float = -1.0f,
    duration: Long = 500L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }

    val r = if (radius >= 0) radius
    else max(
        hypot(x.toDouble(), y.toDouble()),
        hypot((width - x.toDouble()), (height - y.toDouble()))
    ).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, r).setDuration(duration)
    anim.startDelay = offset
    anim.onStart {

    }
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            visible()
            onStart?.invoke()
        }

        override fun onAnimationEnd(animation: Animator) = onFinish?.invoke() ?: Unit
        override fun onAnimationCancel(animation: Animator) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}


fun View.circularHide(
    x: Int = 0,
    y: Int = 0,
    offset: Long = 0L,
    radius: Float = -1.0f,
    duration: Long = 500L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }

    val r = if (radius >= 0) radius
    else hypot(x.toDouble(), y.toDouble()).coerceAtLeast(
        hypot(
            (width - x.toDouble()),
            (height - y.toDouble())
        )
    ).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, r, 0f).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) = onStart?.invoke() ?: Unit

        override fun onAnimationEnd(animation: Animator) {
            invisible()
            onFinish?.invoke() ?: Unit
        }

        override fun onAnimationCancel(animation: Animator) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}

fun View.fadeIn(
    offset: Long = 0L,
    duration: Long = 200L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) = onFinish?.invoke() ?: Unit
        override fun onAnimationStart(animation: Animation?) {
            visible()
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

fun View.fadeOut(
    offset: Long = 0L,
    duration: Long = 200L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            invisible()
            onFinish?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

fun TextView.setTextWithFade(text: String, duration: Long = 200, onFinish: (() -> Unit)? = null) {
    fadeOut(duration = duration, onFinish = {
        setText(text)
        fadeIn(duration = duration, onFinish = onFinish)
    })
}

fun TextView.setTextWithFade(
    @StringRes textId: Int,
    duration: Long = 200,
    onFinish: (() -> Unit)? = null
) =
    setTextWithFade(context.getString(textId), duration, onFinish)

fun ViewPropertyAnimator.scaleXY(value: Float) = scaleX(value).scaleY(value)


//easier value animator
fun Int.animateTo(end: Int, duration: Long = 400, func: (value: Int) -> Unit) {
    ValueAnimator.ofInt(this, end).apply {
        this.duration = duration
        addUpdateListener { valueAnimator ->
            func(valueAnimator.animatedValue as Int)
        }
        start()
    }
}

fun Animator.addListener(
    onEnd: (Animator) -> Unit = {},
    onStart: (Animator) -> Unit = {},
    onCancel: (Animator) -> Unit = {},
    onRepeat: (Animator) -> Unit = {}
) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) {
            onRepeat(animator)
        }

        override fun onAnimationEnd(animator: Animator) {
            onEnd(animator)
        }

        override fun onAnimationCancel(animator: Animator) {
            onCancel(animator)
        }

        override fun onAnimationStart(animator: Animator) {
            onStart(animator)
        }
    })
}

fun Animator.onStart(onStart: (Animator) -> Unit) {
    addListener(onStart = onStart)
}

fun Animator.onEnd(onEnd: (Animator) -> Unit) {
    addListener(onEnd = onEnd)
}

fun Animator.onCancel(onCancel: (Animator) -> Unit) {
    addListener(onCancel = onCancel)
}

fun Animator.onRepeat(onRepeat: (Animator) -> Unit) {
    addListener(onRepeat = onRepeat)
}


fun Animator.addPauseListener(
    onResume: (Animator) -> Unit = {},
    onPause: (Animator) -> Unit = {}
): Animator.AnimatorPauseListener {
    val listener = object : Animator.AnimatorPauseListener {
        override fun onAnimationPause(animator: Animator) {
            onPause(animator)
        }

        override fun onAnimationResume(animator: Animator) {
            onResume(animator)
        }
    }
    addPauseListener(listener)
    return listener
}

@RequiresApi(19)
fun Animator.onResume(onResume: (Animator) -> Unit) {
    addPauseListener(onResume = onResume)
}


fun Animator.onPause(onPause: (Animator) -> Unit) {
    addPauseListener(onPause = onPause)
}

fun View.animateTranslationX(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateTranslationY(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}


fun View.animateTranslationZ(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleX(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleY(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateAlpha(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotation(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationX(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationY(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateX(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateY(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}


fun View.animateZ(
    values: FloatArray,
    duration: Long = 300,
    repeatCount: Int = 0,
    repeatMode: Int = 0
) {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

