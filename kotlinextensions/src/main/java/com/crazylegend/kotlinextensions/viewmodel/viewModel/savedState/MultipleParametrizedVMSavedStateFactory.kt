package com.crazylegend.kotlinextensions.viewmodel.viewModel.savedState

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard, on the calling class
 */
class MultipleParametrizedVMSavedStateFactory(private val constructorParams: Array<out Any>,
                                               owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {


    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return when (constructorParams.size) {
            0 -> modelClass.newInstance()
            1 -> modelClass.getConstructor(handle::class.java).newInstance(handle)
            else -> {
                val parameterClasses =
                        constructorParams.map { param -> param::class.javaPrimitiveType ?: param::class.java }.toList().toTypedArray()
                modelClass.getConstructor(handle::class.java, *parameterClasses).newInstance(handle, *constructorParams)
            }
        }
    }
}