package com.crazylegend.viewbinding

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


inline fun <T : ViewBinding> Activity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }


fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T, disposeEvents: T.() -> Unit = {}) =
        FragmentViewBindingDelegate(this, viewBindingFactory, disposeEvents)


fun <T : ViewBinding> globalViewBinding(viewBindingFactory: (View) -> T) =
        GlobalViewBindingDelegate(viewBindingFactory)

