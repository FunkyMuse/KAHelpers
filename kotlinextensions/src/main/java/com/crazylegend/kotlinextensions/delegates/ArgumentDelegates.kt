/*
package com.crazylegend.kotlinextensions.delegates

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.bundle.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


*/
/**
 * Created by crazy on 11/13/19 to long live and prosper !
 *//*


@Suppress("UNCHECKED_CAST")
class ArgumentDelegates<T : Any?> : ReadWriteProperty<Fragment, T?> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        val key = property.name
        return thisRef.arguments?.get(key) as? T
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        //value?.let { args.put(key, it) } ?: args.remove(key)
    }
}*/
