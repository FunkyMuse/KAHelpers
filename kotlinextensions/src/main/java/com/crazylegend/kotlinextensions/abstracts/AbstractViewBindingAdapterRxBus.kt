package com.crazylegend.kotlinextensions.abstracts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.rx.RxBus
import com.crazylegend.kotlinextensions.views.inflater
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 * Takes leverage of not providing that damn layout res id
 */
abstract class AbstractViewBindingAdapterRxBus<T, VH : RecyclerView.ViewHolder, VB : ViewBinding>(
        private val viewHolder: Class<VH>,
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null }
) :
        ListAdapter<T, VH>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {
    abstract fun bindItems(item: T, holder: VH, position: Int, itemCount: Int)


    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = getItem(holder.adapterPosition)
        bindItems(item, holder, position, itemCount)
    }

    class LongClick<T>(val data: T)
    class SingleClick<T>(val data: T)

    /**
     * Use proguard rule as the following
    -keep public class mypackagename.ViewHolder { public <init>(...); }
     * or annotate it with the @Keep method from androidX
     * @param parent ViewGroup
     * @param viewType Int
     * @return VH
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = bindingInflater.invoke(parent.inflater, parent, false)
        val holder = setViewHolder(binding)

        holder.itemView.setOnClickListenerCooldown {
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                RxBus.publish(SingleClick(getItem(holder.adapterPosition)))
        }
        holder.itemView.setOnLongClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                RxBus.publish(LongClick(getItem(holder.adapterPosition)))
            true
        }
        return holder
    }


    @Suppress("UNCHECKED_CAST")
    private fun setViewHolder(binding: ViewBinding): VH = viewHolder.declaredConstructors.first().newInstance(binding) as VH
}