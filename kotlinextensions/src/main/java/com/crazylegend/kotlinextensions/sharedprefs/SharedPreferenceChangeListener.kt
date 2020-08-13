package com.crazylegend.kotlinextensions.sharedprefs

import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


/**
 * Created by crazy on 8/13/20 to long live and prosper !
 */
class SharedPreferenceChangeListener(
        private val sharedPreferences: SharedPreferences,
        private val listener: (SharedPreferences, String) -> Unit
) : SharedPreferences.OnSharedPreferenceChangeListener, LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        listener(sharedPreferences, key)
    }
}

fun SharedPreferences.registerSharedPreferenceChangeListener(
        owner: LifecycleOwner,
        listener: (SharedPreferences, String) -> Unit
) {
    owner.lifecycle.addObserver(SharedPreferenceChangeListener(this, listener))
}
