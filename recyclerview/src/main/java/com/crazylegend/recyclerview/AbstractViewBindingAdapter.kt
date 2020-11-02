package com.crazylegend.recyclerview

import android.view.LayoutInflater
import android.view.View
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
abstract class AbstractViewBindingAdapter<T, VH : RecyclerView.ViewHolder, VB : ViewBinding>(
        private val viewHolder: (binding: VB) -> VH,
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null }
) :
        ListAdapter<T, VH>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {
    abstract fun bindItems(item: T, holder: VH, position: Int, itemCount: Int)

    var forItemClickListener: forItemClickListener<T>? = null
    var onLongClickListener: forItemClickListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = getItem(holder.adapterPosition)
        bindItems(item, holder, position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val holder = setViewHolder(binding)

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

    /**
     * Sets an on click listener for a view, but ensures the action cannot be triggered more often than [coolDown] milliseconds.
     */
    private inline fun View.setOnClickListenerCooldown(coolDown: Long = 1000L, crossinline action: (view: View) -> Unit) {
        setOnClickListener(object : View.OnClickListener {
            var lastTime = 0L
            override fun onClick(v: View) {
                val now = System.currentTimeMillis()
                if (now - lastTime > coolDown) {
                    action(v)
                    lastTime = now
                }
            }
        })
    }


    @Suppress("UNCHECKED_CAST")
    private fun setViewHolder(binding: ViewBinding): VH = viewHolder(binding as VB)
}