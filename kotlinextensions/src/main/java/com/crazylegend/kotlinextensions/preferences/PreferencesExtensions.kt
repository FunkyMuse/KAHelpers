package com.crazylegend.kotlinextensions.preferences

import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat


/**
 * Created by crazy on 4/30/20 to long live and prosper !
 */

fun SwitchPreferenceCompat?.booleanChangeListener(action: (preference: Preference, newValue: Boolean) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as Boolean
        action(pref, value)
        true
    }
}

fun ListPreference?.stringChangeListener(action: (preference: Preference, newValue: String) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as String
        action(pref, value)
        true
    }
}

fun Preference?.onClick(action: () -> Unit = {}) {
    this?.setOnPreferenceClickListener {
        action()
        true
    }
}