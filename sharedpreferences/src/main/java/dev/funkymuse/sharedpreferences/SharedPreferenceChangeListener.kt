package dev.funkymuse.sharedpreferences

import android.content.SharedPreferences
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner



class SharedPreferenceChangeListener(
        private val sharedPreferences: SharedPreferences,
        private val listener: (SharedPreferences?, String?) -> Unit
) : SharedPreferences.OnSharedPreferenceChangeListener, DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        listener(sharedPreferences, key)
    }
}


