package com.crazylegend.kotlinextensions.activity

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

/**
 * Hide keyboard for an activity.
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
            .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

