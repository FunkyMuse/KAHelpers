package com.crazylegend.viewbinding

import android.app.Activity
import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


inline fun <T : ViewBinding> Activity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T, disposeRecyclerViewsAutomatically: Boolean = true) =
        FragmentViewBindingDelegate(this, viewBindingFactory, disposeRecyclerViewsAutomatically)


fun <T : ViewBinding> globalViewBinding(viewBindingFactory: (View) -> T) =
        GlobalViewBindingDelegate(viewBindingFactory)

internal fun ensureMainThread() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw IllegalThreadStateException("Views can only be accessed on the main thread.")
    }
}

fun ViewBinding?.disposeRecyclers() {
    val viewGroup = this?.root ?: return
    if (viewGroup is ViewGroup) {
        viewGroup.children.filterIsInstance<RecyclerView>().forEach {
            it.adapter = null
        }
    }
}