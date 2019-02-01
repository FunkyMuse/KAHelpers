package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
interface onItemClickListener {
    fun onItemClicked(view: View, position:Int)
}