package com.crazylegend.kotlinextensions.insets

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat


/**
 * Created by crazy on 8/20/20 to long live and prosper !
 */

val View.isKeyboardVisible: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = WindowInsetsCompat
            .toWindowInsetsCompat(rootWindowInsets)
            .isVisible(WindowInsetsCompat.Type.ime())
