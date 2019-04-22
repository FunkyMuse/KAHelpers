package com.crazylegend.kotlinextensions.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.crazylegend.kotlinextensions.context.layoutInflater


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

fun Context.inflate(
        @LayoutRes layoutId: Int,
        root: ViewGroup? = null,
        attachToRoot: Boolean = false
): View? = layoutInflater?.inflate(layoutId, root, attachToRoot)



