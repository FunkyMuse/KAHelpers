package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */
interface forItemClickListener<T> {
    fun forItem(position:Int, item:T, purpose:ClickPurpose, view: View)
}

fun <T> forItemClickListenerDSL(callback: (position: Int, item: T, purpose: ClickPurpose, view: View) -> Unit = { _, _, _, _ -> }): forItemClickListener<T> {
    return object : forItemClickListener<T> {
        override fun forItem(position: Int, item: T, purpose: ClickPurpose, view: View) {
            callback(position, item, purpose, view)
        }
    }
}
