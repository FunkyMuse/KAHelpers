package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */
fun interface forItemClickListener<T> {
    fun forItem(position: Int, item: T, view: View)
}

