package com.crazylegend.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by crazy on 10/25/20 to long live and prosper !
 */
class ActivityViewBindingDelegate<T : ViewBinding>(
        private val activity: AppCompatActivity,
        private val viewBinder: (LayoutInflater) -> T,
        private val beforeSetContent: () -> Unit = {}
) : ReadOnlyProperty<AppCompatActivity, T>, LifecycleObserver {

    private var activityBinding: T? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun createBinding() {
        initialize()
        beforeSetContent()
        activity.setContentView(activityBinding?.root)
        activity.lifecycle.removeObserver(this)
    }

    private fun initialize() {
        if (activityBinding == null) {
            activityBinding = viewBinder(activity.layoutInflater)
        }
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        ensureMainThread()

        initialize()
        return activityBinding!!
    }


}