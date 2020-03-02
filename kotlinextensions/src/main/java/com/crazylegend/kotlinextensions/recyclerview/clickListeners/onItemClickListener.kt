package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by crazy on 3/2/20 to long live and prosper !
 */
interface onItemClickListener {
    fun onItem(position: Int, view: View)
}

fun onItemClickListenerDSL(clickedOn: (positionWhereClicked: Int, viewClicked: View) -> Unit = { _, _ -> }) = object : onItemClickListener {
    override fun onItem(position: Int, view: View) {
        clickedOn(position, view)
    }
}