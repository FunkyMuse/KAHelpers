package com.crazylegend.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * Created by crazy on 3/9/20 to long live and prosper !
 */


inline fun <reified T, VB : ViewBinding> generateRecyclerWithHolder(
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline onCreateBinding: (binding:VB, position:Int) -> Unit = {_,_->},
                crossinline binder: (item: T, position: Int, itemCount: Int, binding: VB, context: Context) -> Unit): AbstractViewBindingHolderAdapter<T, VB> {

    return object : AbstractViewBindingHolderAdapter<T, VB>(bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, onCreateBinding) {
        override fun bindItems(item: T, position: Int, itemCount: Int, binding: VB, context: Context) {
            binder(item, position, itemCount, binding, context)
        }
    }
}

inline fun <reified T, VB : ViewBinding> RecyclerView.generateVerticalAdapterWithHolder(
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline onCreateBinding: (binding:VB, position:Int) -> Unit = {_,_->},
        crossinline binder: (item: T, position: Int, itemCount: Int, binding: VB, context: Context) -> Unit,
        hasFixedSize: Boolean = false, reverseLayout: Boolean = false): AbstractViewBindingHolderAdapter<T, VB> {

    val adapter = generateRecyclerWithHolder(bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, onCreateBinding, binder)
    initRecyclerViewAdapter(adapter, RecyclerView.VERTICAL, hasFixedSize, reverseLayout)
    return adapter
}

inline fun <reified T, VB : ViewBinding> RecyclerView.generateHorizontalAdapterWithHolder(
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline onCreateBinding: (binding:VB, position:Int) -> Unit = {_,_->},
        crossinline binder: (item: T, position: Int, itemCount: Int, binding: VB, context: Context) -> Unit,
        hasFixedSize: Boolean = false, reverseLayout: Boolean = false): AbstractViewBindingHolderAdapter<T, VB> {

    val adapter = generateRecyclerWithHolder(bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback, onCreateBinding, binder)
    initRecyclerViewAdapter(adapter, RecyclerView.HORIZONTAL, hasFixedSize, reverseLayout)
    return adapter
}


