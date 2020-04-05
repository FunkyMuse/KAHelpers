package com.crazylegend.kotlinextensions.viewBinding

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 3/3/20 to long live and prosper !
 */
class RecyclerViewBindingDelegate<T : ViewBinding>(val viewBinder: (View) -> T) : ReadOnlyProperty<RecyclerView.ViewHolder, T> {

    override fun getValue(thisRef: RecyclerView.ViewHolder, property: KProperty<*>): T {
        return viewBinder(thisRef.itemView)
    }
}