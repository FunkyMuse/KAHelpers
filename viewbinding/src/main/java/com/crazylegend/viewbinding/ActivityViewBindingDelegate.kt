package com.crazylegend.viewbinding

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by crazy on 10/25/20 to long live and prosper !
 */
@RequiresApi(Build.VERSION_CODES.Q)
class ActivityViewBindingDelegate<T : ViewBinding>(
        private val appCompatActivity: AppCompatActivity,
        private val viewBinder: (LayoutInflater) -> T,
        private val beforeSetContent: () -> Unit = {}
) : ReadOnlyProperty<AppCompatActivity, T> {

    private var activityBinding: T? = null

    init {
        appCompatActivity.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                createBinding()
            }
            override fun onActivityStarted(p0: Activity) {}

            override fun onActivityResumed(p0: Activity) {}

            override fun onActivityPaused(p0: Activity) {}

            override fun onActivityStopped(p0: Activity) {}

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

            override fun onActivityDestroyed(p0: Activity) {}
        })
    }

    private fun createBinding() {
        initialize()
        beforeSetContent()
        appCompatActivity.setContentView(activityBinding?.root)
    }

    private fun initialize() {
        if (activityBinding == null) {
            activityBinding = viewBinder(appCompatActivity.layoutInflater)
        }
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        ensureMainThread()

        initialize()
        return activityBinding!!
    }


}