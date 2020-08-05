package com.crazylegend.kotlinextensions.viewmodel.androidViewModel.savedState

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard, on the calling class
 */
class MultipleParametrizedAVMSavedStateFactory(private val constructorParams: Array<out Any>,
                                               private val application: Application,
                                               owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {


    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        val parameterClasses =
                constructorParams.map { param ->
                    param::class.javaPrimitiveType ?: param::class.java
                }.toList().toTypedArray()
        return modelClass.getConstructor(Application::class.java, handle::class.java, *parameterClasses).newInstance(application, handle, *constructorParams)
    }
}