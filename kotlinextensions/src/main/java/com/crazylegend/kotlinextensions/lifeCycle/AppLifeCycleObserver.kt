package com.crazylegend.kotlinextensions.lifeCycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**
 * Created by hristijan on 3/18/19 to long live and prosper !
 */

/**
 * USAGE
 *
val appLifeCycleObserver = AppLifeCycleObserver()

lifecycle.addObserver(appLifeCycleObserver)

appLifeCycleObserver.lifeCycleCallback = object : LifeCycleCallback{
override fun appInBackground() {
appLifeCycleObserver.debug("App in background")
}

override fun appInForeground() {
appLifeCycleObserver.debug("App in Foreground")
}
}
 *
 *
 */
class AppLifeCycleObserver : LifecycleObserver {

    var lifeCycleCallback : LifeCycleCallBacks ? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        lifeCycleCallback?.appInForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground(){
        lifeCycleCallback?.appInBackground()
    }



}

