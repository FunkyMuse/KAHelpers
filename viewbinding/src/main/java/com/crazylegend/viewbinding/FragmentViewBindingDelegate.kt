package com.crazylegend.viewbinding


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(private val fragment: Fragment,
                                                   private val viewBinder: (View) -> T,
                                                   private val disposeEvents: T.() -> Unit = {}) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private inline fun Fragment.observeLifecycleOwnerThroughLifecycleCreation(crossinline viewOwner: LifecycleOwner.() -> Unit) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                viewLifecycleOwnerLiveData.observe(this@observeLifecycleOwnerThroughLifecycleCreation, Observer { viewLifecycleOwner ->
                    viewLifecycleOwner.viewOwner()
                })
            }
        })
    }

    private var fragmentBinding: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeBinding() {
        fragmentBinding?.disposeEvents()
        fragmentBinding = null
    }

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreation {
            lifecycle.addObserver(this@FragmentViewBindingDelegate)
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {

        ensureMainThread()

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
