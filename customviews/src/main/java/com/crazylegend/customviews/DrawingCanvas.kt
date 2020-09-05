package com.crazylegend.customviews


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

/**
 * Created by crazy on 8/7/20 to long live and prosper !
 */

class DrawingCanvas @JvmOverloads constructor(context: Context,
                                              attributeSet: AttributeSet? = null,
                                              defStyleAttr: Int = -1) : View(context, attributeSet, defStyleAttr) {

    var isTouchEventListenerEnabled = true
    private var isInScale = false
    private val pointerIdToPathMap = mutableMapOf<Int, Path>()
    private val finishedPointerPaths = mutableListOf<Path>()
    private val paint = Paint().apply {
        strokeWidth = 3.toFloat()
        style = Paint.Style.STROKE
    }
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            clear()
            return true
        }
    })

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            isInScale = true
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            isInScale = false
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            detector?.let {
                scaleX *= detector.scaleFactor
                scaleY *= detector.scaleFactor
            }
            return true
        }
    })

    override fun onDraw(canvas: Canvas?) {
        pointerIdToPathMap.forEach { (_, path) ->
            canvas?.drawPath(path, paint)
        }
        finishedPointerPaths.forEach { path ->
            canvas?.drawPath(path, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isTouchEventListenerEnabled) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        if (!isInScale) {
            when (event?.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val pointerIdToAdd = event.getPointerId(event.actionIndex)
                    val pathToAdd = Path().apply {
                        moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
                    }
                    pointerIdToPathMap[pointerIdToAdd] = pathToAdd
                }
                MotionEvent.ACTION_MOVE -> {
                    for (i in 0 until event.pointerCount) {
                        val path = pointerIdToPathMap[event.getPointerId(i)]
                        path?.lineTo(event.getX(i), event.getY(i))
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                    val pointerToRemove = event.getPointerId(event.actionIndex)
                    val pathToRemove = pointerIdToPathMap[pointerToRemove]
                    if (pathToRemove != null) {
                        finishedPointerPaths.add(pathToRemove)
                    }
                    pointerIdToPathMap.remove(pointerToRemove)
                }
                else -> {
                    return false
                }
            }
        }

        invalidate()
        return true
    }

    fun clear() {
        pointerIdToPathMap.clear()
        finishedPointerPaths.clear()
        invalidate()
    }
}