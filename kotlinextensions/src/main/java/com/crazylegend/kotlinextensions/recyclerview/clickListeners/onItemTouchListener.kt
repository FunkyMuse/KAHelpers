package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.MotionEvent
import android.view.View


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
interface onItemTouchListener {
    fun onTouch(view: View, event: MotionEvent, position: Int)
}