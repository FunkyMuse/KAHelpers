package com.crazylegend.recyclerview

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

fun RecyclerView.fullScreenGestureNavigation(fragmentActivity: FragmentActivity, topInset: Int = 200, bottomInset: Int = 40) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Disable clipping of the RecyclerView content when we use padding
            clipToPadding = false

            // Make the Gesture Navigation Bar transparent
            fragmentActivity.window.navigationBarColor = Color.TRANSPARENT

            // Expand the Views (RecyclerView) under the gesture navigation bar and toolbar
            fragmentActivity.window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

            ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                view.updatePadding(
                    top = insets.systemGestureInsets.top + topInset,
                    bottom = insets.systemGestureInsets.bottom + bottomInset
                )
                insets
            }
    }
}


fun RecyclerView.fullScreenGestureNavigation(fragment: Fragment) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Disable clipping of the RecyclerView content when we use padding
            clipToPadding = false

            // Make the Gesture Navigation Bar transparent
            fragment.requireActivity().window.navigationBarColor = Color.TRANSPARENT

            // Expand the Views (RecyclerView) under the gesture navigation bar and toolbar
            fragment.requireActivity().window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

            // Set padding for the Views (RecyclerView and Relative Layout) from the System Views (Gesture Navigation Bar, Toolbar)
            setOnApplyWindowInsetsListener { _, insets ->
                val topPadding = insets.systemWindowInsetTop
                val bottomPadding = insets.systemWindowInsetBottom
                setPadding(0, topPadding, 0, 0)
                setPadding(0, 0, 0, bottomPadding)
                insets.consumeSystemWindowInsets()
            }
    }
}