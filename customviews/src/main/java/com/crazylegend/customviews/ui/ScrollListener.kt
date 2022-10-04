package com.crazylegend.customviews.ui

import android.content.res.Resources
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


open class ScrollListener(val width: () -> Int, val height: () -> Int) : GestureDetector.SimpleOnGestureListener() {

    private val density: Float
        get() = Resources.getSystem().displayMetrics.density

    private fun dpToPx(dp: Int): Int {
        val scale = density
        return (dp * scale + 0.5f).toInt()
    }


    var onScroll: ((percentX: Float, percentY: Float) -> Unit)? = null

    /**
     * Min Swipe X-Distance
     */
    var thresholdX = dpToPx(3)

    /**
     * Min Swipe Y-Distance
     */
    var thresholdY = dpToPx(3)

    /**
     * Starting X-Position
     */
    protected var startX = 0f

    /**
     * Starting Y-Position
     */
    protected var startY = 0f

    override fun onDown(e: MotionEvent): Boolean {
        startX = e.x
        startY = e.y
        return true
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        val dX = startX - e2.x
        val dY = startY - e2.y

        return when {
            abs(dX) <= thresholdX && abs(dY) <= thresholdY -> false
            else -> {

                val percentX = (dX) / width()
                val percentY = (dY) / height()

                onScroll?.invoke(percentX, percentY)

                true
            }
        }
    }
}
