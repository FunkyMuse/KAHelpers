package dev.funkymuse.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <reified T, VH : RecyclerView.ViewHolder, VB : ViewBinding> generateRecycler(
        noinline viewHolder: (binding: VB) -> VH,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int, itemCount: Int) -> Unit): AbstractViewBindingAdapter<T, VH, VB> {

    return object : AbstractViewBindingAdapter<T, VH, VB>(viewHolder, bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback) {
        override fun bindItems(item: T, holder: VH, position: Int, itemCount: Int) {
            binder(item, holder, position, itemCount)
        }
    }
}


inline fun <reified T, VH : RecyclerView.ViewHolder, VB : ViewBinding> RecyclerView.generateVerticalAdapter(
        noinline viewHolder: (binding: VB) -> VH,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int, itemCount: Int) -> Unit,
        hasFixedSize: Boolean = false, reverseLayout: Boolean = false): AbstractViewBindingAdapter<T, VH, VB> {

    val adapter = generateRecycler(viewHolder, bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, binder)
    initRecyclerViewAdapter(adapter, RecyclerView.VERTICAL, hasFixedSize, reverseLayout)
    return adapter
}

inline fun <reified T, VH : RecyclerView.ViewHolder, VB : ViewBinding> RecyclerView.generateHorizontalAdapter(
        noinline viewHolder: (binding: VB) -> VH,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int, itemCount: Int) -> Unit,
        hasFixedSize: Boolean = false, reverseLayout: Boolean = false): AbstractViewBindingAdapter<T, VH, VB> {

    val adapter = generateRecycler(viewHolder, bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, binder)
    initRecyclerViewAdapter(adapter, RecyclerView.HORIZONTAL, hasFixedSize, reverseLayout)
    return adapter
}


