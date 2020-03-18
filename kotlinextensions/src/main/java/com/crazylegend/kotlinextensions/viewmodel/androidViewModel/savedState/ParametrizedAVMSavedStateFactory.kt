package com.crazylegend.kotlinextensions.viewmodel.androidViewModel.savedState

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.reflect.Constructor


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard
 * You should keep the order as is in the constructor create call, first your application, then handle and then params
 */
class ParametrizedAVMSavedStateFactory(private val application: Application,
                                       private val param: Any,
                                       owner: SavedStateRegistryOwner,
                                       defaultArgs: Bundle? = null) :
        AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        val type = param::class.javaPrimitiveType ?: param::class.java
        val constructor: Constructor<T> = modelClass.getDeclaredConstructor(application::class.java, handle::class.java, type)
        return constructor.newInstance(application, handle, param)
    }
}