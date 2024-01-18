package com.funkymuse.kotlinextensions.preferences

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class PreferenceDelegate<T : Preference>(private val key: String) :
    ReadOnlyProperty<PreferenceFragmentCompat, T> {

    override fun getValue(thisRef: PreferenceFragmentCompat, property: KProperty<*>): T {
        return thisRef.findPreference(key)
            ?: throw IllegalArgumentException("The preference with the key = $key you've set couldn't be found, please check the key!")
    }
}

fun <T : Preference> preference(key: String) = PreferenceDelegate<T>(key)