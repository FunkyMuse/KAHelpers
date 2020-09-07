package com.crazylegend.kotlinextensions.misc

import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.fragments.observeLifecycleOwnerThroughLifecycleCreationOnDestroy
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */
class AutoClearedValue<T : Any>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {
    private var _value: T? = null

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreationOnDestroy {
            _value = null
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException("Value cleared")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }
}