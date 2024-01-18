package com.funkymuse.customviews

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat


class ScalingView : FrameLayout, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private var onClickListener: OnClickListener? = null
    private lateinit var gestureDetectorCompat: GestureDetectorCompat
    private val MAX_CLICK_DISTANCE = 2

    private var x1 = 0f
    private var y1 = 0f
    private var x2 = 0f
    private var y2 = 0f
    private var dx = 0f
    private var dy = 0f

    private var longPressed = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    @SuppressLint("ClickableViewAccessibility")
    fun init() {

        gestureDetectorCompat = GestureDetectorCompat(context, this)
        gestureDetectorCompat.setOnDoubleTapListener(this)


        setOnTouchListener { _: View, event: MotionEvent ->

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    y1 = event.y
                }
                MotionEvent.ACTION_MOVE -> checkIfThresholdMoved(event)
                MotionEvent.ACTION_CANCEL -> handleCancel()
                MotionEvent.ACTION_UP -> {
                    if (longPressed)
                        onLongPressConfirmed()
                }
            }

            gestureDetectorCompat.onTouchEvent(event)
            true
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        onClickListener = l
    }

    private fun onLongPressConfirmed() {
        scaleOriginal()
        longPressed = false
        onClickListener?.onClick(this)
    }


    private fun handleCancel() {
        longPressed = false
        scaleOriginal()
    }

    private fun checkIfThresholdMoved(event: MotionEvent) {

        x2 = event.x
        y2 = event.y
        dx = x2 - x1
        dy = y2 - y1

        if (dx > MAX_CLICK_DISTANCE || dy > MAX_CLICK_DISTANCE)
            scaleOriginal()
    }

    override fun onDoubleTap(p0: MotionEvent): Boolean {
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent): Boolean {
        scaleOriginal()
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        scaleDown()
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent) {
        longPressed = true
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        scaleDown()
        onClickListener?.onClick(this)
        return true
    }


    private fun scaleOriginal() {
        val scaleUpX = ObjectAnimator.ofFloat(
                this, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(
                this, "scaleY", 1f)
        scaleUpX.duration = 200
        scaleUpY.duration = 200
        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)
        scaleUp.start()
    }

    private fun scaleDown() {

        val scaleDownX = ObjectAnimator.ofFloat(this,
                "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(this,
                "scaleY", 0.9f)
        scaleDownX.duration = 200
        scaleDownY.duration = 200
        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        scaleDown.start()
    }

}