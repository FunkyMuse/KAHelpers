package dev.funkymuse.customviews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View


class RippleCanvas @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val eraser: Paint = Paint().apply {
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private var baseColor = Color.TRANSPARENT
    private val ripples: MutableList<Ripple> = mutableListOf()

    /**
     * Draw ripples one at a time in the order given
     * To support transparent ripples, we simply erase the overlapping base before adding a new circle
     */
    override fun onDraw(canvas: Canvas) {
        paint.color = baseColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        val itr = ripples.iterator()
        while (itr.hasNext()) {
            val r = itr.next()
            paint.color = r.color
            canvas.drawCircle(r.x, r.y, r.radius, eraser)
            canvas.drawCircle(r.x, r.y, r.radius, paint)
            if (r.radius == r.maxRadius) {
                itr.remove()
                baseColor = r.color
            }
        }
    }

    /**
     * Creates a ripple effect from the given starting values
     */
    fun ripple(
            color: Int,
            startX: Float = 0f,
            startY: Float = 0f,
            duration: Long = 600L,
            callback: (() -> Unit)? = null
    ) {
        val w = width.toFloat()
        val h = height.toFloat()
        val x = when (startX) {
            MIDDLE -> w / 2
            END -> w
            else -> startX
        }
        val y = when (startY) {
            MIDDLE -> h / 2
            END -> h
            else -> startY
        }
        val maxRadius = Math.hypot(Math.max(x, w - x).toDouble(), Math.max(y, h - y).toDouble()).toFloat()
        val ripple = Ripple(color, x, y, 0f, maxRadius)
        ripples.add(ripple)
        val animator = ValueAnimator.ofFloat(0f, maxRadius)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            ripple.radius = animation.animatedValue as Float
            invalidate()
        }
        if (callback != null)
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) = callback()
                override fun onAnimationEnd(animation: Animator) = callback()
            })
        animator.start()
    }

    /**
     * Sets a color directly; clears ripple queue if it exists
     */
    fun set(color: Int) {
        baseColor = color
        ripples.clear()
        invalidate()
    }

    override fun setBackgroundColor(color: Int) = set(color)

    /**
     * Sets a color directly but with a transition
     */
    fun fade(color: Int, duration: Long = 300L) {
        ripples.clear()
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), baseColor, color)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            baseColor = animation.animatedValue as Int
            invalidate()
        }
        animator.start()
    }

    internal class Ripple(
            val color: Int,
            val x: Float,
            val y: Float,
            var radius: Float,
            val maxRadius: Float
    )

    companion object {
        const val MIDDLE = -1.0f
        const val END = -2.0f
    }
}