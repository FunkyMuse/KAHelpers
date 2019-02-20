package com.crazylegend.kotlinextensions.recyclerview.clickListeners

import android.view.View


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */
interface forItemClickListener<T> {
    fun forItem(position:Int, item:T, purpose:ClickPurpose, view: View)
}