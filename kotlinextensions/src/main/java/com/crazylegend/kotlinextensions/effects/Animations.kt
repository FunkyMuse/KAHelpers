package com.crazylegend.kotlinextensions.effects

import android.animation.*
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.*
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.transition.Scene
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.packageutils.buildIsLollipopAndUp
import com.crazylegend.kotlinextensions.views.afterLatestMeasured
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.invisible
import com.crazylegend.kotlinextensions.views.visible
import kotlin.math.hypot


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

fun slideLeftRight(toHideView: View, fromView: View, toShowView: View, switchViews: Boolean) {

    if (switchViews) {
        if (toShowView.visibility == View.GONE) {
            toShowView.visibility = View.VISIBLE
        }
        val params =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
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

        val translateAnimationRight = TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
        translateAnimationRight.duration = 1000
        translateAnimationRight.fillAfter = true
        toShowView.startAnimation(translateAnimationRight)
    } else {
        val params =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
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

        val translateAnimationRight = TranslateAnimation((-fromView.width / 2).toFloat(), 0f, 0f, 0f)
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
    if (!buildIsLollipopAndUp) return fadeIn(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else Math.max(
            Math.hypot(x.toDouble(), y.toDouble()),
            Math.hypot((width - x.toDouble()), (height - y.toDouble()))
    ).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, r).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            visible()
            onStart?.invoke()
        }

        override fun onAnimationEnd(animation: Animator?) = onFinish?.invoke() ?: Unit
        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
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
    if (!buildIsLollipopAndUp) return fadeOut(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else hypot(x.toDouble(), y.toDouble()).coerceAtLeast(hypot((width - x.toDouble()), (height - y.toDouble()))).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, r, 0f).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) = onStart?.invoke() ?: Unit

        override fun onAnimationEnd(animation: Animator?) {
            invisible()
            onFinish?.invoke() ?: Unit
        }

        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
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

fun TextView.setTextWithFade(@StringRes textId: Int, duration: Long = 200, onFinish: (() -> Unit)? = null) =
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

@RequiresApi(19)
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

@RequiresApi(19)
fun Animator.onPause(onPause: (Animator) -> Unit) {
    addPauseListener(onPause = onPause)
}

fun View.animateTranslationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateTranslationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateTranslationZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateScaleY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateAlpha(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotation(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateRotationY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateX(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.animateY(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.animateZ(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0) {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    animator.start()
}

fun View.translationXAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.translationYAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.translationZAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleXAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.scaleYAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.SCALE_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.alphaAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ALPHA, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationXAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.rotationYAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.ROTATION_Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.xAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.X, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.yAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Y, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.zAnimator(values: FloatArray, duration: Long = 300, repeatCount: Int = 0, repeatMode: Int = 0): Animator {
    val animator = ObjectAnimator.ofFloat(this, View.Z, *values)
    animator.repeatCount = repeatCount
    animator.duration = duration
    if (repeatMode == ObjectAnimator.REVERSE || repeatMode == ObjectAnimator.RESTART) {
        animator.repeatMode = repeatMode
    }
    return animator
}

fun View.getHeightAnimator(endHeight: Int, callback: ((Int) -> Unit)? = null): ValueAnimator {
    val heightAnimator = ValueAnimator.ofInt(height, endHeight)
    heightAnimator.addUpdateListener {
        val value = it.animatedValue as Int
        callback?.invoke(value)
        layoutParams.height = value
        requestLayout()
    }
    return heightAnimator
}

fun View.getWidthAnimator(endWidth: Int, callback: ((Int) -> Unit)? = null): ValueAnimator {
    val widthAnimator = ValueAnimator.ofInt(width, endWidth)
    widthAnimator.addUpdateListener {
        val value = it.animatedValue as Int
        callback?.invoke(value)
        layoutParams.width = value
        requestLayout()
    }
    return widthAnimator
}

fun ViewPropertyAnimator.scale(factor: Float) = apply {
    scaleX(factor)
    scaleY(factor)
}

fun ViewPropertyAnimator.scaleBy(factor: Float) = apply {
    scaleXBy(factor)
    scaleYBy(factor)
}

fun ViewPropertyAnimator.translation(distance: Float) = apply {
    translationY(distance)
    translationX(distance)
}

fun ViewPropertyAnimator.translationBy(distance: Float) = apply {
    translationXBy(distance)
    translationYBy(distance)
}

fun AnimatorSet.playWith(vararg items: Animator) = apply {
    playTogether(items.toMutableList())
}

fun Animator.playWith(animator: Animator): Animator {
    if (animator is AnimatorSet)
        return animator.playWith(this)
    else if (this is AnimatorSet)
        return this.playWith(animator)
    return AnimatorSet().playWith(this, animator)
}


inline fun View.animator(build: ViewPropertyAnimator.() -> Unit)
        : ViewPropertyAnimator = animate().apply(build)

inline fun ViewPropertyAnimator.scale(factor: () -> Float) = scale(factor())

inline fun ViewPropertyAnimator.scaleBy(factor: () -> Float) = scaleBy(factor())

inline fun ViewPropertyAnimator.translation(factor: () -> Float) = translation(factor())

inline fun ViewPropertyAnimator.translationBy(factor: () -> Float) = translationBy(factor())

fun AbsListView.setEdgeEffectColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        val edgeEffect = EdgeEffect(context).apply { this.color = color }
        javaClass.getDeclaredField("mEdgeGlowTop").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
        javaClass.getDeclaredField("mEdgeGlowBottom").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
    }
}

fun ScrollView.setEdgeEffectColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        val edgeEffect = EdgeEffect(context).apply { this.color = color }
        javaClass.getDeclaredField("mEdgeGlowTop").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
        javaClass.getDeclaredField("mEdgeGlowBottom").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
    }
}

fun NestedScrollView.setEdgeEffectColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        val edgeEffect = EdgeEffect(context).apply { this.color = color }
        javaClass.getDeclaredField("mEdgeGlowTop").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
        javaClass.getDeclaredField("mEdgeGlowBottom").apply {
            isAccessible = true
            set(this@setEdgeEffectColor, edgeEffect)
        }
    }
}

inline fun ViewPropertyAnimator.setAnimationListener(func: AnimatorListenerImpl.() -> Unit): ViewPropertyAnimator {
    val listener = AnimatorListenerImpl(this)
    listener.func()
    setListener(listener)
    return this
}

class AnimatorListenerImpl(private val viewPropertyAnimator: ViewPropertyAnimator) :
        Animator.AnimatorListener {

    private var _onAnimationRepeat: ((viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit)? = null
    private var _onAnimationEnd: ((viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit)? = null
    private var _onAnimationCancel: ((viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit)? = null
    private var _onAnimationStart: ((viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit)? = null

    fun onAnimationEnd(func: (viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit) {
        _onAnimationEnd = func
    }

    fun onAnimationStart(func: (viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit) {
        _onAnimationStart = func
    }

    fun onAnimationCancel(func: (viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit) {
        _onAnimationCancel = func
    }

    fun onAnimationRepeat(func: (viewPropertyAnimator: ViewPropertyAnimator, animation: Animator) -> Unit) {
        _onAnimationRepeat = func
    }

    override fun onAnimationRepeat(animation: Animator) {
        _onAnimationRepeat?.invoke(viewPropertyAnimator, animation)
    }

    override fun onAnimationEnd(animation: Animator) {
        _onAnimationEnd?.invoke(viewPropertyAnimator, animation)
    }

    override fun onAnimationCancel(animation: Animator) {
        _onAnimationCancel?.invoke(viewPropertyAnimator, animation)
    }

    override fun onAnimationStart(animation: Animator) {
        _onAnimationStart?.invoke(viewPropertyAnimator, animation)
    }
}


fun ViewGroup.startTransition(transition: TransitionSet) {
    TransitionManager.go(Scene(this), transition)
}

fun View.rotate(rotation: Float, animated: Boolean = true, animationDuration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()) {
    when (animated) {
        true -> this.animate().rotationBy(rotation)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(animationDuration)
                .start()
        false -> this.rotation = this.rotation + rotation
    }
}

// ViewPropertyAnimator is singleton, so it is needed to reset settings in order to chain animations as wanted
fun View.animate(reset: Boolean = false): ViewPropertyAnimator {
    return this.animate().reset()
}

fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun ViewPropertyAnimator.reset(): ViewPropertyAnimator {
    return this
            .setListener(null)
            .setDuration(400)
            .setStartDelay(0)
            .setInterpolator(LinearInterpolator())
}


fun View?.fadeIn(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        if (alpha > 0f) {
            alpha = 0f
        }

        if (!isVisible) {
            visible()
        }

        return animate(true)
                .alpha(1.0f)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)

    }

    return null
}

/**
 * Fades out the View
 */
fun View?.fadeOut(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        return animate(true)
                .alpha(0.0f)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .withEndAction {
                    gone()
                }
    }

    return null
}

/**
 * Fades to a specific alpha between 0 to 1
 */
fun View?.fadeTo(alpha: Float, duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        return animate(true)
                .alpha(alpha)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Enter from left
 */
fun View?.enterFromLeft(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        this.invisible()
        val x = this.x    // store initial x
        this.x = 0f - this.width    // move to left outside screen
        this.visible()

        return animate(true).x(x)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Enter from right
 */
fun View?.enterFromRight(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        this.invisible()
        val widthPixels = Resources.getSystem().displayMetrics.widthPixels    // get device width
        val x = this.x    // store initial x
        this.x = widthPixels.toFloat()    // move to right outside screen
        this.visible()

        return animate(true).x(x)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Enter from top
 */
fun View?.enterFromTop(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        this.invisible()
        val y = this.y    // store initial y
        this.y = 0f - this.height    // move to top
        this.visible()

        return animate(true).y(y)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }
    return null
}

/**
 * Animation: Enter from bottom
 */
fun View?.enterFromBottom(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        this.invisible()
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()    // get device height
        val y = this.y          // store initial y
        this.y = screenHeight   // move to bottom
        this.visible()

        return animate().y(y)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Exit to left
 */
fun View?.exitToLeft(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        return animate(true).x(0f - this.width)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Exit to right
 */
fun View?.exitToRight(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        val widthPixels = Resources.getSystem().displayMetrics.widthPixels    // get device width
        return animate(true).x(widthPixels.toFloat())
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }

    return null
}

/**
 * Animation: Exit to top
 */
fun View?.exitToTop(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        val y = this.y    // store initial y
        return animate(true).y(0f - this.height)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration).withEndAction {
                    // reset to original pos
                    this.y = y
                    this.invisible()
                }
    }

    return null
}

/**
 * Animation: Exit to bottom
 */
fun View?.exitToBottom(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        val heightPixels = Resources.getSystem().displayMetrics.heightPixels    // get device height
        val y = this.y  // store initial y

        return animate(true).y(heightPixels.toFloat())
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration).withEndAction {
                    this.y = y
                    this.gone()
                }
    }

    return null
}

/**
 * Animation: Slide up its own height to its original position
 */
fun View?.slideUp(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator()): ViewPropertyAnimator? {
    this?.let {
        this.invisible()
        this.translationY = this.height.toFloat()
        this.visible()

        return animate(true)
                .translationY(0f)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .setDuration(duration)
    }
    return null
}


fun entryAnimationFromBottom(view: View, duration: Long = 600, finished: () -> Unit = {}) {
    view.alpha = 0f

    AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(view, "translationY", 50f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f)
        )
        this.duration = duration
        interpolator = DecelerateInterpolator()
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                finished()
            }

            override fun onAnimationCancel(animation: Animator?) {
                finished()
            }

            override fun onAnimationStart(animation: Animator?) {}

        })
    }.start()
}


fun enterChildViewsFromBottomDelayed(
        view: ViewGroup,
        delay: Long = 150,
        duration: Long = 500,
        finished: () -> Unit = {}
) {
    for (i in 0 until view.childCount) {
        view.getChildAt(i).alpha = 0f
    }

    val animators = (0 until view.childCount).mapIndexed { index, value ->
        AnimatorSet().apply {
            playTogether(
                    ObjectAnimator.ofFloat(view.getChildAt(index), "translationY", 50f, 0f),
                    ObjectAnimator.ofFloat(view.getChildAt(index), "alpha", 0f, 1.0f)
            )
            startDelay = index * delay
            this.duration = duration
            interpolator = DecelerateInterpolator()
        }
    }

    AnimatorSet().apply {
        playTogether(animators)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                finished()
            }

            override fun onAnimationCancel(animation: Animator?) {
                finished()
            }

            override fun onAnimationStart(animation: Animator?) {}

        })
    }.start()
}


/**
 * Fades out the View
 */
fun View?.fadeOut(duration: Long = 400, startDelay: Long = 0, interpolator: Interpolator = AccelerateDecelerateInterpolator(), invisible: Boolean = false): ViewPropertyAnimator? {
    this?.let {
        return animate(true)
                .alpha(0.0f)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(interpolator)
                .withEndAction {
                    if (!invisible) this.gone() else this.invisible()
                }
    }

    return null
}


fun View.setbackgroundColorResourceAnimated(resId: Int, duration: Long = 400) {
    val colorFrom = (this.background as ColorDrawable).color
    val colorTo = this.context.getColorCompat(resId)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = duration
    colorAnimation.addUpdateListener { animator -> this.setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
}

fun View.fadeInUp(duration: Long = 250, offset: Float? = null) {
    this.invisible()
    afterLatestMeasured {
        this.translationY = offset ?: this.height.toFloat() - (this.height / 2)
        this.alpha = 0f
        this.visible()
        this.animate(true).translationY(0f).alpha(1f).setDuration(duration).interpolator = AccelerateDecelerateInterpolator()
    }
}

fun View.circularRevealEnter() {
    val cx = width / 2
    val cy = height / 2

    val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius)

    visibility = View.VISIBLE
    anim.start()
}

fun View.circularRevealExit() {
    val cx = width / 2
    val cy = height / 2

    val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, initialRadius, 0f)

    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            visibility = View.INVISIBLE
        }
    })

    anim.start()
}