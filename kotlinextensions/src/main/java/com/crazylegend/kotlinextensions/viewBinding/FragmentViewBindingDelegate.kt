package com.crazylegend.kotlinextensions.viewBinding


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.fragments.observeLifecycleOwnerThroughLifecycleCreation
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(private val fragment: Fragment, private val viewBinder: (View) -> T) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var fragmentBinding: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeBinding() {
        fragmentBinding = null
    }

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreation {
            lifecycle.addObserver(this@FragmentViewBindingDelegate)
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = fragmentBinding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Fragment views are destroyed.")
        }
        return viewBinder(thisRef.requireView()).also { fragmentBinding = it }
    }
}
