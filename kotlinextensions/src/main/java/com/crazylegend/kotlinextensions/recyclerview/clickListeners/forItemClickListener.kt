package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */
interface forItemClickListener<T> {
    fun forItem(position: Int, item: T, view: View)
}

inline fun <T> forItemClickListenerDSL(crossinline callback: (position: Int, item: T, view: View) -> Unit = { _, _, _ -> }) =
        object : forItemClickListener<T> {
            override fun forItem(position: Int, item: T, view: View) {
                callback(position, item, view)
            }
        }

