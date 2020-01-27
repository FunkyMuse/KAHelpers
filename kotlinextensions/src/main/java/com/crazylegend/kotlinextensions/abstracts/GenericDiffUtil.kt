package com.crazylegend.kotlinextensions.abstracts

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil


/**
 * Created by crazy on 1/27/20 to long live and prosper !
 */
class GenericDiffUtil<T>(private val areItemsTheSameCallback: (old: T, new: T) -> Boolean?,
                         private val areContentsTheSameCallback: (old: T, new: T) -> Boolean?) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSameCallback(oldItem, newItem) ?: newItem == oldItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSameCallback(oldItem, newItem) ?: newItem == oldItem

}