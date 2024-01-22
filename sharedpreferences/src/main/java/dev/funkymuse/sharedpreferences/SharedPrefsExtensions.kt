package dev.funkymuse.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LifecycleOwner




fun SharedPreferences.putString(key: String, value: String) {
    edit { putString(key, value) }
}

fun SharedPreferences.commitString(key: String, value: String) {
    edit(true) { putString(key, value) }
}

fun SharedPreferences.putInt(key: String, value: Int) {
    edit { putInt(key, value) }
}

fun SharedPreferences.commitInt(key: String, value: Int) {
    edit(true) { putInt(key, value) }
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit { putBoolean(key, value) }
}

fun SharedPreferences.commitBoolean(key: String, value: Boolean) {
    edit(true) { putBoolean(key, value) }
}

fun SharedPreferences.putFloat(key: String, value: Float) {
    edit { putFloat(key, value) }
}

fun SharedPreferences.commitFloat(key: String, value: Float) {
    edit(true) { putFloat(key, value) }
}

fun SharedPreferences.putLong(key: String, value: Long) {
    edit { putLong(key, value) }
}

fun SharedPreferences.commitLong(key: String, value: Long) {
    edit(true) { putLong(key, value) }
}

fun SharedPreferences.putStringSet(key: String, value: Set<String>) {
    edit { putStringSet(key, value) }
}

fun SharedPreferences.commitStringSet(key: String, value: Set<String>) {
    edit(true) { putStringSet(key, value) }
}


fun SharedPreferences.clear() {
    edit { clear() }
}

fun SharedPreferences.commitClear() {
    edit(true) { clear() }
}

fun SharedPreferences.remove(key: String) {
    edit { remove(key) }
}

fun SharedPreferences.commitRemoval(key: String) {
    edit(true) { remove(key) }
}

fun <T> SharedPreferences.put(key: String, t: T) {
    when (t) {
        is Int -> putInt(key, t)
        is Long -> putLong(key, t)
        is Float -> putFloat(key, t)
        is String -> putString(key, t)
        is Boolean -> putBoolean(key, t)
    }
}

fun <T> SharedPreferences.commit(key: String, t: T) {
    when (t) {
        is Int -> commitInt(key, t)
        is Long -> commitLong(key, t)
        is Float -> commitFloat(key, t)
        is String -> commitString(key, t)
        is Boolean -> commitBoolean(key, t)
    }
}

inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T): T =
        when (defaultValue) {
            is Boolean -> getBoolean(key, defaultValue) as? T ?: defaultValue
            is Int -> getInt(key, defaultValue) as? T ?: defaultValue
            is Float -> getFloat(key, defaultValue) as? T? ?: defaultValue
            is Long -> getLong(key, defaultValue) as? T? ?: defaultValue
            is String -> getString(key, defaultValue) as? T? ?: defaultValue
            else -> throw UnsupportedOperationException("Class not supported by SharedPreferences.put()")
        }

fun SharedPreferences.registerSharedPreferenceChangeListener(
        owner: LifecycleOwner,
        listener: (SharedPreferences?, String?) -> Unit
) {
    owner.lifecycle.addObserver(SharedPreferenceChangeListener(this, listener))
}