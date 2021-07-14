package com.crazylegend.recyclerview

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
abstract class AbstractViewBindingAdapterSingleSelection<T, VH : RecyclerView.ViewHolder, VB : ViewBinding>(
        private val viewHolder: (binding: VB) -> VH,
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        private val onCreateBinding: (holder: VH) -> Unit = {}

) :
        ListAdapter<T, VH>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {
    abstract fun bindItems(item: T, holder: VH, position: Int, itemCount: Int)

    var selectedPosition = -1
    var forItemClickListener: forItemClickListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = getItem(holder.bindingAdapterPosition)
        bindItems(item, holder, position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val holder = setViewHolder(binding)
        onCreateBinding(holder)

        holder.itemView.setOnClickListenerCooldown {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION)
            {
                forItemClickListener?.forItem(holder.bindingAdapterPosition, getItem(holder.bindingAdapterPosition), it)
                notifyItemChanged(selectedPosition)
                selectedPosition = holder.bindingAdapterPosition
                notifyItemChanged(selectedPosition)
            }
        }

        return holder
    }

    @Suppress("UNCHECKED_CAST")
    private fun setViewHolder(binding: ViewBinding): VH = viewHolder(binding as VB)
}