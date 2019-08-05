package com.crazylegend.kotlinextensions.views

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


/**
 * Created by hristijan on 3/7/19 to long live and prosper !
 */

fun View.snackbar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbar(@StringRes msg: Int) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbarLong(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
}

fun View.snackbarLong(@StringRes msg: Int) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
}

fun View.snackbarIndefinite(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE).show()
}

fun View.snackbarIndefinite(@StringRes msg: Int) {
    Snackbar.make(this, msg, Snackbar.LENGTH_INDEFINITE).show()
}

fun Snackbar.withTextColor(color: Int): Snackbar {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(color)
    return this
}

inline fun View.snack(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(@StringRes actionRes: Int, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), null, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


/**
 * Returns the [Snackbar]'s text view by looking for
 * `android.support.design.R.id.snackbar_text` in the view's layout.
 */
val Snackbar.textView: TextView?
    get() = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView

/**
 * Default [Snackbar] duration. Set to [Snackbar.LENGTH_LONG].
 */
const val SNACKBAR_DEFAULT_DURATION = Snackbar.LENGTH_LONG
