package dev.funkymuse.kotlinextensions.insets

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

val View.isKeyboardVisible: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = WindowInsetsCompat
        .toWindowInsetsCompat(rootWindowInsets)
        .isVisible(WindowInsetsCompat.Type.ime())

val View.imeHeight
    @RequiresApi(Build.VERSION_CODES.R)
    get() = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets)
        .getInsets(WindowInsetsCompat.Type.ime()).bottom


fun View.hideKeyboard() {
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
}

fun View.showKeyboard() {
    ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())
}

fun View.showKeyboardAndFocus() {
    ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())
    requestFocus()
}

fun View.imeVisibilityListener(onVisibilityChanged: (Boolean) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        onVisibilityChanged(insets.isVisible(WindowInsetsCompat.Type.ime()))
        insets
    }
}