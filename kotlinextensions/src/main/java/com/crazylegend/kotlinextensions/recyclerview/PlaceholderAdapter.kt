package com.crazylegend.kotlinextensions.recyclerview

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
abstract class PlaceholderAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

}