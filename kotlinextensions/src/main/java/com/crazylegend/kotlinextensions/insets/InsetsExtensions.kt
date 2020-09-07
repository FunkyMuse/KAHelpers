package com.crazylegend.kotlinextensions.insets

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


/**
 * Created by crazy on 8/20/20 to long live and prosper !
 */

val View.isKeyboardVisible: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = WindowInsetsCompat
            .toWindowInsetsCompat(rootWindowInsets)
            .isVisible(WindowInsetsCompat.Type.ime())

val View.imeHeight
    @RequiresApi(Build.VERSION_CODES.R)
    get() = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets)
            .getInsets(WindowInsetsCompat.Type.ime()).bottom


@RequiresApi(Build.VERSION_CODES.R)
fun View.showKeyboard() = windowInsetsController?.show(WindowInsetsCompat.Type.ime())

@RequiresApi(Build.VERSION_CODES.R)
fun View.hideKeyboard() = windowInsetsController?.hide(WindowInsetsCompat.Type.ime())


fun View.imeVisibilityListener(onVisibilityChanged: (Boolean) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        onVisibilityChanged(insets.isVisible(WindowInsetsCompat.Type.ime()))
        insets
    }
}