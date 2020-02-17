package com.crazylegend.kotlinextensions.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * Created by crazy on 2/14/20 to long live and prosper !
 */


class OrientationAwareRecyclerView(context: Context, attrs: AttributeSet?,
                                   defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var scrolling = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val lm = layoutManager ?: return super.onInterceptTouchEvent(e)
        var allowScroll = true
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = e.x
                lastY = e.y
                // If we were scrolling, stop now by faking a touch release
                if (scrolling) {
                    val newEvent = MotionEvent.obtain(e)
                    newEvent.action = MotionEvent.ACTION_UP
                    return super.onInterceptTouchEvent(newEvent)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // We're moving, so check if we're trying
                // to scroll vertically or horizontally so we don't intercept the wrong event.
                val currentX = e.x
                val currentY = e.y
                val dx = abs(currentX - lastX)
                val dy = abs(currentY - lastY)
                allowScroll = if (dy > dx) lm.canScrollVertically() else lm.canScrollHorizontally()
            }
        }
        return if (!allowScroll) {
            false
        } else super.onInterceptTouchEvent(e)
    }

    init {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrolling = newState != SCROLL_STATE_IDLE
            }
        })
    }
}