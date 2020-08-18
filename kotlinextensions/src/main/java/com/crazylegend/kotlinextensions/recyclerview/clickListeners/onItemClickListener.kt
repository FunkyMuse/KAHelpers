package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by crazy on 3/2/20 to long live and prosper !
 */
fun interface onItemClickListener {
    fun onItem(atPosition: Int, view: View)
}