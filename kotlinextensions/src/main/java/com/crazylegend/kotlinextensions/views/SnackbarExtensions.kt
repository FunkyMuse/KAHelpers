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