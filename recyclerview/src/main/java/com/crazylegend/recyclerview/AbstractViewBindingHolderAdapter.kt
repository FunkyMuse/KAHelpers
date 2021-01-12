package com.crazylegend.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crazylegend.recyclerview.clickListeners.forItemClickListener


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 * Takes leverage of not providing that damn layout res id
 *
 * USAGE:
 * class TestViewBindingAdapter : AbstractViewBindingAdapter<TestModel, TestViewHolderShimmer, CustomizableCardViewBinding>(
::TestViewHolderShimmer, CustomizableCardViewBinding::inflate
)
 *
 */
abstract class AbstractViewBindingHolderAdapter<T, VB : ViewBinding>(
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null }
) :
        ListAdapter<T, AbstractViewBindingHolderAdapter.AbstractViewHolder<VB>>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {

    var forItemClickListener: forItemClickListener<T>? = null
    var onLongClickListener: forItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<VB> {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val holder = AbstractViewHolder(binding)

        holder.itemView.setOnClickListenerCooldown {
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                forItemClickListener?.forItem(holder.adapterPosition, getItem(holder.adapterPosition), it)
        }
        holder.itemView.setOnLongClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                onLongClickListener?.forItem(holder.adapterPosition, getItem(holder.adapterPosition), it)
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<VB>, position: Int) {
        val item = getItem(holder.adapterPosition)
        bindItems(item, position, itemCount, holder.binding, holder.itemView.context)
    }

    abstract fun bindItems(item: T, position: Int, itemCount: Int, binding: VB, context: Context)

    class AbstractViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}