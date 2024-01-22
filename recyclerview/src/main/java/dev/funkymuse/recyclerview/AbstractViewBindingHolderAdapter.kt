package dev.funkymuse.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
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
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        private val onCreateBinding: (holder: AbstractViewHolder<VB>) -> Unit = {}
) :
        ListAdapter<T, AbstractViewBindingHolderAdapter.AbstractViewHolder<VB>>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {

    var forItemClickListener: ((position: Int, item: T, view: View) -> Unit)? = null
    var onLongClickListener: ((position: Int, item: T, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<VB> {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val holder = AbstractViewHolder(binding)
        onCreateBinding(holder)

        holder.itemView.setOnClickListenerCooldown {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION)
                forItemClickListener?.invoke(holder.bindingAdapterPosition, getItem(holder.bindingAdapterPosition), it)
        }
        holder.itemView.setOnLongClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION)
                onLongClickListener?.invoke(holder.bindingAdapterPosition, getItem(holder.bindingAdapterPosition), it)
            true
        }
        return holder
    }


    override fun onBindViewHolder(holder: AbstractViewHolder<VB>, position: Int) {
        val item = getItem(holder.bindingAdapterPosition)
        bindItems(item, position, itemCount, holder.binding, holder.itemView.context)
    }

    abstract fun bindItems(item: T, position: Int, itemCount: Int, binding: VB, context: Context)

    class AbstractViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}