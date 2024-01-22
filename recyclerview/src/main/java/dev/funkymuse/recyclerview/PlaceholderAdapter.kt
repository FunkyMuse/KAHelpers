package dev.funkymuse.recyclerview

import androidx.recyclerview.widget.RecyclerView



abstract class PlaceholderAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    override fun getItemCount() = Int.MAX_VALUE

}