package com.crazylegend.viewbinding

import android.app.Activity
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


inline fun <T : ViewBinding> Activity.viewBinder(crossinline bindingInflater: (LayoutInflater) -> T) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

fun <T : ViewBinding> AppCompatActivity.viewBinding(bindingInflater: (LayoutInflater) -> T,
                                                    beforeSetContent: () -> Unit = {}) =
        ActivityViewBindingDelegate(this, bindingInflater, beforeSetContent)


fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T, disposeEvents: T.() -> Unit = {}) =
        FragmentViewBindingDelegate(this, viewBindingFactory, disposeEvents)


fun <T : ViewBinding> globalViewBinding(viewBindingFactory: (View) -> T) =
        GlobalViewBindingDelegate(viewBindingFactory)

internal fun ensureMainThread() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw IllegalThreadStateException("View can be accessed only on the main thread.")
    }
}