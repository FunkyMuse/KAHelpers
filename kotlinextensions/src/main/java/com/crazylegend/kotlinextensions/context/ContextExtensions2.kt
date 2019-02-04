package com.crazylegend.kotlinextensions.context

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

fun Activity?.hideKeyboard() {
    if (this != null) {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        if (inputManager != null) {
            val v = this.currentFocus
            if (v != null) {
                inputManager.hideSoftInputFromWindow(
                    v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}

fun Context.showKeyboard(toFocus: View) {
    toFocus.requestFocus()
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // imm.showSoftInput(toFocus, InputMethodManager.SHOW_IMPLICIT);
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
}
