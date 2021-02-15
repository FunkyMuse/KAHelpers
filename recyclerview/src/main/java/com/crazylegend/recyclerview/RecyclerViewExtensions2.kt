package com.crazylegend.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionManager


/**
 * Created by hristijan on 1/17/21 to long live and prosper !
 */

inline fun RecyclerView.hideOnScroll(threshold: Int = 20,
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

fun RecyclerView.replaceAdapterWith(replacementAdapter: RecyclerView.Adapter<*>, transition: Transition? = null, onAnimator: (RecyclerView.ItemAnimator?) -> Unit) {
    if (adapter != replacementAdapter) {
        adapter = replacementAdapter
        onAnimator(itemAnimator)
        itemAnimator = null
        transition?.let { TransitionManager.beginDelayedTransition(this, it) }
    }
}

fun RecyclerView.clearOnDetach(){
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
        override fun onViewAttachedToWindow(v: View?) {
        }

        override fun onViewDetachedFromWindow(v: View?) {
            adapter = null
        }
    })
}