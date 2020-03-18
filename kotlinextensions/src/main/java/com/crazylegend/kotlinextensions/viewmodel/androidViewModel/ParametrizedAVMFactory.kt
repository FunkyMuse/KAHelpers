package com.crazylegend.kotlinextensions.viewmodel.androidViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.Constructor


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard
 */
class ParametrizedAVMFactory(private val application: Application, private val param: Any) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val type = param::class.javaPrimitiveType ?: param::class.java
        val constructor: Constructor<T> = modelClass.getDeclaredConstructor(application::class.java, type)
        return constructor.newInstance(application, param)
    }
}