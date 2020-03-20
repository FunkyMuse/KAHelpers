package com.crazylegend.kotlinextensions.viewmodel.androidViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 * You should keep the fields when using proguard, on the calling class
 */
class MultipleParametrizedAVMFactory(private val constructorParams: Array<out Any>,
                                     private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val parameterClasses = constructorParams.map { param -> param::class.javaPrimitiveType ?: param::class.java }.toList().toTypedArray()
        return modelClass.getConstructor(application::class.java, *parameterClasses).newInstance(application, *constructorParams)
    }
}