package com.crazylegend.recyclerview

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by crazy on 4/15/20 to long live and prosper !
 */
abstract class HideOnScrollListener(private val HIDE_THRESHOLD: Int = 20) : RecyclerView.OnScrollListener() {

    private var scrolledDistance = 0
    private var controlsVisible = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide()
            controlsVisible = false
            scrolledDistance = 0
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow()
            controlsVisible = true
            scrolledDistance = 0
        }
        if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
            scrolledDistance += dy
        }

    }

    abstract fun onHide()
    abstract fun onShow()
}