package com.crazylegend.kotlinextensions.preferences

import androidx.preference.DialogPreference
import androidx.preference.Preference
import androidx.preference.TwoStatePreference


/**
 * Created by crazy on 4/30/20 to long live and prosper !
 */

fun TwoStatePreference?.booleanChangeListener(action: (preference: Preference, newValue: Boolean) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as Boolean
        action(pref, value)
        true
    }
}

fun DialogPreference?.stringChangeListener(action: (preference: Preference, newValue: String) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as String
        action(pref, value)
        true
    }
}

fun DialogPreference?.booleanChangeListener(action: (preference: Preference, newValue: Boolean) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as Boolean
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

fun Preference?.disable() {
    this?.isEnabled = false
}

fun Preference?.enable() {
    this?.isEnabled = true
}

fun TwoStatePreference?.disableAndUnCheck() {
    this?.isEnabled = false
    this?.isChecked = false
}

fun TwoStatePreference?.enableAndCheck() {
    this?.isEnabled = true
    this?.isChecked = true
}

fun Preference?.disableCopying() {
    this?.isCopyingEnabled = false
}

fun Preference?.enableCopying() {
    this?.isCopyingEnabled = true
}
