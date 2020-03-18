package com.crazylegend.kotlinextensions.viewmodel.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard
 */
class ParametrizedVMFactory(private val param: Any) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val type = param::class.javaPrimitiveType ?: param::class.java
        val constructor = modelClass.getDeclaredConstructor(type)
        return constructor.newInstance(param)
    }
}