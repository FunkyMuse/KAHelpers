package com.crazylegend.kotlinextensions.views

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible


/**
 * Created by crazy on 7/24/20 to long live and prosper !
 */

inline fun View.ifVisible(action: () -> Unit) {
    if (isVisible) action()
}


inline fun View.ifInvisible(action: () -> Unit) {
    if (isInvisible) action()
}


inline fun View.ifGone(action: () -> Unit) {
    if (isGone) action()
}


