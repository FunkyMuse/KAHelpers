package com.funkymuse.viewbinding


import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(private val fragment: Fragment,
                                                   private val viewBinder: (View) -> T,
                                                   private val disposeRecyclerViewsAutomatically: Boolean = true) : ReadOnlyProperty<Fragment, T> {

    private val mainHandler = Handler(Looper.getMainLooper())

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

    private fun disposeBinding() {
        if (disposeRecyclerViewsAutomatically)
            fragmentBinding.disposeRecyclers()

        mainHandler.post { fragmentBinding = null }
    }

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreation {
            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    disposeBinding()
                }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {

        ensureMainThread()

        val binding = fragmentBinding
        if (binding != null && thisRef.view === binding.root) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Fragment views are destroyed.")
        }
        return viewBinder(thisRef.requireView()).also { fragmentBinding = it }
    }
}

