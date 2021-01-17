package com.crazylegend.recyclerview

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by hristijan on 1/17/21 to long live and prosper !
 */

inline fun RecyclerView.hideOnScroll(threshold: Int,
                                     crossinline hide: () -> Unit = {},
                                     crossinline show: () -> Unit = {}) {
    addOnScrollListener(object : HideOnScrollListener(threshold) {
        override fun onHide() {
            hide()
        }

        override fun onShow() {
            show()
        }
    })
}