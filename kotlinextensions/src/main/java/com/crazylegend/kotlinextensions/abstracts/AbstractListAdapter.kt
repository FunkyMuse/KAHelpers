package com.crazylegend.kotlinextensions.abstracts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.kotlinextensions.views.inflate
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown


/**
 * Created by hristijan on 8/16/19 to long live and prosper !
 */
abstract class AbstractListAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>,
                                                                    private val viewHolder: Class<VH>) : ListAdapter<T, VH>(diffCallback) {

    abstract val getLayout: Int
    abstract fun bindItems(item: T, holder: VH, position: Int)

    var forItemClickListener: forItemClickListener<T>? = null
    var onLongClickListener: forItemClickListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = getItem(position)
        bindItems(item, holder, position)
        holder.itemView.setOnClickListenerCooldown {
            forItemClickListener?.forItem(position, item, it)
        }
        holder.itemView.setOnLongClickListener {
            it?.let { view -> onLongClickListener?.forItem(position, item, view = view) }
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = setViewHolder(parent.inflate(getLayout))

    private fun setViewHolder(inflatedView: View): VH = viewHolder.declaredConstructors.first().newInstance(inflatedView) as VH

}