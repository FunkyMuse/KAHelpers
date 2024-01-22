package dev.funkymuse.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil


/**
 * Use [T] as data class preferably if you don't pass anything in constructors
 * @param T
 * @property areItemsTheSameCallback Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Boolean?>
 * @property areContentsTheSameCallback Function2<[@kotlin.ParameterName] T, [@kotlin.ParameterName] T, Boolean?>
 * @constructor
 */
class GenericDiffUtil<T>(
    private val areItemsTheSameCallback: (old: T, new: T) -> Boolean?,
    private val areContentsTheSameCallback: (old: T, new: T) -> Boolean?
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areItemsTheSameCallback(oldItem, newItem) ?: newItem == oldItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
        areContentsTheSameCallback(oldItem, newItem) ?: newItem == oldItem

}

fun <T> diffUtilDSL(
    areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
    areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null }
) = GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)