package com.funkymuse.kotlinextensions.preferences

import androidx.preference.DialogPreference
import androidx.preference.Preference
import androidx.preference.TwoStatePreference

inline fun TwoStatePreference?.booleanChangeListener(crossinline action: (preference: Preference, newValue: Boolean) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as Boolean
        action(pref, value)
        true
    }
}

inline fun DialogPreference?.stringChangeListener(crossinline action: (preference: Preference, newValue: String) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as String
        action(pref, value)
        true
    }
}

inline fun DialogPreference?.booleanChangeListener(crossinline action: (preference: Preference, newValue: Boolean) -> Unit = { _, _ -> }) {
    this?.setOnPreferenceChangeListener { pref, value ->
        value as Boolean
        action(pref, value)
        true
    }
}

inline fun Preference?.onClick(crossinline action: () -> Unit = {}) {
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

fun TwoStatePreference?.unCheck() {
    this?.isChecked = false
}

fun TwoStatePreference?.check() {
    this?.isChecked = true
}