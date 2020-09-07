package com.crazylegend.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 3/3/20 to long live and prosper !
 */
class GlobalViewBindingDelegate<T : ViewBinding>(val viewBinder: (View) -> T) : ReadOnlyProperty<View, T> {

    override fun getValue(thisRef: View, property: KProperty<*>): T {
        return viewBinder(thisRef)
    }
}