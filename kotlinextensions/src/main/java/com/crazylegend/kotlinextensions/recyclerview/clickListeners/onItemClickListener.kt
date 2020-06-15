package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by crazy on 3/2/20 to long live and prosper !
 */
interface onItemClickListener {
    fun onItem(atPosition: Int, view: View)
}

inline fun onItemClickListenerDSL(crossinline callback: (position: Int, view: View) -> Unit = { _, _ -> }) = object :
        onItemClickListener{
    override fun onItem(atPosition: Int, view: View) {
        callback(atPosition, view)
    }
}