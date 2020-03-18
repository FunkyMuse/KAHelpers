package com.crazylegend.kotlinextensions.input

import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import kotlin.math.abs


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */
abstract class FlingGestureHandler @JvmOverloads constructor(
        activity: Activity,
        private val minDistance: Int = 100,
        private val velocityThreshold: Int = 100
) : View.OnTouchListener {


    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(activity, GestureListener())
    }

    override fun onTouch(v: View,  event: MotionEvent): Boolean {
        return !gestureDetector.onTouchEvent(event)
    }

    abstract fun onRightToLeft()

    abstract fun onLeftToRight()

    abstract fun onBottomToTop()

    abstract fun onTopToBottom()

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
                 e1: MotionEvent,  e2: MotionEvent, velocityX: Float,
                velocityY: Float
        ): Boolean {
            if (e1.x - e2.x > minDistance && abs(velocityX) > velocityThreshold) {
                onRightToLeft()
                return true
            } else if (e2.x - e1.x > minDistance && abs(velocityX) > velocityThreshold) {
                onLeftToRight()
                return true
            }
            if (e1.y - e2.y > minDistance && abs(velocityY) > velocityThreshold) {
                onBottomToTop()
                return true
            } else if (e2.y - e1.y > minDistance && abs(velocityY) > velocityThreshold) {
                onTopToBottom()
                return true
            }
            return false
        }
    }

}