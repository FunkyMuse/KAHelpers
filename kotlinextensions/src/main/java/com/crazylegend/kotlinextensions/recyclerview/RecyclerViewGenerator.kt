package com.crazylegend.kotlinextensions.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter


/**
 * Created by crazy on 3/9/20 to long live and prosper !
 */


inline fun <reified T, VH : RecyclerView.ViewHolder, VB : ViewBinding> generateRecycler(
        viewHolder: Class<VH>,
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
        viewHolder: Class<VH>,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T, holder: VH, position: Int, itemCount: Int) -> Unit): AbstractViewBindingAdapter<T, VH, VB> {

    val adapter = generateRecycler(viewHolder, bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, binder)
    initRecyclerViewAdapter(adapter, RecyclerView.VERTICAL)
    return adapter
}


