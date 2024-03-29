package dev.funkymuse.customviews

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal fun Context.getSharedPreferencesByTag(tag: String) = getSharedPreferences(tag, Context.MODE_PRIVATE)


internal fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit { putBoolean(key, value) }
}

internal fun FragmentManager.remove(vararg removeFragment: Fragment) {
    val ft = this.beginTransaction()
    for (fragment in removeFragment) {
        ft.remove(fragment)
    }
    ft.commit()
}

internal fun Fragment.remove() {
    this.parentFragmentManager.remove(this)
}


internal inline fun Boolean.ifTrue(function: () -> Unit): Boolean {
    if (this) function()
    return this
}

internal inline fun Boolean.ifFalse(function: () -> Unit): Boolean {
    if (!this) function()
    return this
}


internal fun FragmentManager.removeFragmentWithStateLoss(fragment: Fragment) {
    beginTransaction().remove(fragment).commitNowAllowingStateLoss()
}