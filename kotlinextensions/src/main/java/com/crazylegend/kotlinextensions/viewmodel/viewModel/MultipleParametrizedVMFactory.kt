package com.crazylegend.kotlinextensions.viewmodel.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard, on the calling class
 */
class MultipleParametrizedVMFactory(private val constructorParams: Array<out Any>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val parameterClasses =
                constructorParams.map { param ->
                    param::class.javaPrimitiveType ?: param::class.java
                }.toList().toTypedArray()
        return modelClass.getConstructor(*parameterClasses).newInstance(*constructorParams)
    }
}