package com.crazylegend.kotlinextensions.abstracts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.kotlinextensions.views.inflate
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown


/**
 * Created by hristijan on 8/16/19 to long live and prosper !
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(private val list: List<T>, private val viewHolder: Class<VH>) : RecyclerView.Adapter<VH>() {

    abstract val getLayout: Int
    abstract fun bindItems(item: T, holder: VH, position: Int, itemCount: Int)

    var forItemClickListener: forItemClickListener<T>? = null
    var onLongClickListener: forItemClickListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = list[position]
        bindItems(item, holder, position, list.count())

        holder.itemView.setOnClickListenerCooldown {
            if (position != RecyclerView.NO_POSITION)
                forItemClickListener?.forItem(position, list[position], it)
        }
        holder.itemView.setOnLongClickListener {
            if (position != RecyclerView.NO_POSITION)
                onLongClickListener?.forItem(position, list[position], view = it)
            true
        }
    }

    /**
     * keep the view holder's constructor with { public <init>(...); }
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = setViewHolder(parent.inflate(getLayout))


    private fun setViewHolder(inflatedView: View): VH = viewHolder.declaredConstructors.first().newInstance(inflatedView) as VH


}