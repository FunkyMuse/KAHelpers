package com.crazylegend.recyclerview

import androidx.recyclerview.widget.RecyclerView


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
abstract class PlaceholderAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    override fun getItemCount() = Int.MAX_VALUE

}