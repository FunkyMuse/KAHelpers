package com.crazylegend.kotlinextensions.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.crazylegend.kotlinextensions.viewmodel.androidViewModel.MultipleParametrizedAVMFactory
import com.crazylegend.kotlinextensions.viewmodel.androidViewModel.ParametrizedAVMFactory
import com.crazylegend.kotlinextensions.viewmodel.androidViewModel.savedState.MultipleParametrizedAVMSavedStateFactory
import com.crazylegend.kotlinextensions.viewmodel.androidViewModel.savedState.ParametrizedAVMSavedStateFactory
import com.crazylegend.kotlinextensions.viewmodel.viewModel.MultipleParametrizedVMFactory
import com.crazylegend.kotlinextensions.viewmodel.viewModel.ParametrizedVMFactory
import com.crazylegend.kotlinextensions.viewmodel.viewModel.savedState.MultipleParametrizedVMSavedStateFactory
import com.crazylegend.kotlinextensions.viewmodel.viewModel.savedState.ParametrizedVMSavedStateFactory

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */


//app compat activity
inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(): T {
    return ViewModelProvider(this).get()
}


inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(param: Any): T {
    return ViewModelProvider(this, ParametrizedVMFactory(param)).get()
}

inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(param: Any,
                                                                    defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, ParametrizedVMSavedStateFactory(param, this, defaultArgs)).get()
}


inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(constructorParams: Array<out Any>): T {
    return ViewModelProvider(this, MultipleParametrizedVMFactory(constructorParams)).get()
}


inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(constructorParams: Array<out Any> = emptyArray(),
                                                                    defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, MultipleParametrizedVMSavedStateFactory(constructorParams, this, defaultArgs)).get()
}


inline fun <reified T : AndroidViewModel> AppCompatActivity.compatProviderAVM(param: Any): T {
    return ViewModelProvider(this, ParametrizedAVMFactory(application, param)).get()
}


inline fun <reified T : AndroidViewModel> AppCompatActivity.compatProviderAVM(param: Any,
                                                                              defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, ParametrizedAVMSavedStateFactory(application, param, this, defaultArgs)).get()
}


inline fun <reified T : AndroidViewModel> AppCompatActivity.compatProviderAVM(constructorParams: Array<out Any>): T {
    return ViewModelProvider(this, MultipleParametrizedAVMFactory(constructorParams, application)).get()
}


inline fun <reified T : AndroidViewModel> AppCompatActivity.compatProviderAVM(constructorParams: Array<out Any> = emptyArray(),
                                                                              defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, MultipleParametrizedAVMSavedStateFactory(constructorParams, application, this, defaultArgs)).get()
}


inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get()
}

//fragment
inline fun <reified T : ViewModel> Fragment.fragmentProvider(): T {

    return ViewModelProvider(this).get()
}

inline fun <reified T : ViewModel> Fragment.fragmentProvider(param: Any): T {
    return ViewModelProvider(this, ParametrizedVMFactory(param)).get()
}


inline fun <reified T : ViewModel> Fragment.fragmentProvider(param: Any,
                                                             defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, ParametrizedVMSavedStateFactory(param, this, defaultArgs)).get()
}


inline fun <reified T : ViewModel> Fragment.fragmentProvider(constructorParams: Array<out Any> = emptyArray(),
                                                             defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, MultipleParametrizedVMSavedStateFactory(constructorParams, this, defaultArgs)).get()
}


inline fun <reified T : AndroidViewModel> Fragment.fragmentProviderAVM(param: Any): T {
    return ViewModelProvider(this, ParametrizedAVMFactory(requireActivity().application, param)).get()
}

inline fun <reified T : AndroidViewModel> Fragment.fragmentProviderAVM(constructorParams: Array<out Any> = emptyArray()): T {
    return ViewModelProvider(this, MultipleParametrizedAVMFactory(constructorParams, requireActivity().application)).get()
}


inline fun <reified T : AndroidViewModel> Fragment.fragmentProviderAVM(constructorParams: Array<out Any> = emptyArray(),
                                                                       defaultArgs: Bundle? = null): T {
    return ViewModelProvider(this, MultipleParametrizedAVMSavedStateFactory(constructorParams, requireActivity().application, this, defaultArgs)).get()
}


inline fun <reified T : ViewModel> Fragment.fragmentProvider(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get()
}

//shared model from the same activity
inline fun <reified T : ViewModel> Fragment.sharedProvider(): T {

    return ViewModelProvider(requireActivity()).get()
}


inline fun <reified T : ViewModel> Fragment.sharedProvider(param: Any): T {
    return ViewModelProvider(requireActivity(), ParametrizedVMFactory(param)).get()
}


inline fun <reified T : ViewModel> Fragment.sharedProvider(param: Any, defaultArgs: Bundle? = null): T {
    return ViewModelProvider(requireActivity(), ParametrizedVMSavedStateFactory(param, this, defaultArgs)).get()
}

inline fun <reified T : ViewModel> Fragment.sharedProvider(constructorParams: Array<out Any>): T {
    return ViewModelProvider(requireActivity(), MultipleParametrizedVMFactory(constructorParams)).get()
}

inline fun <reified T : ViewModel> Fragment.sharedProvider(constructorParams: Array<out Any> = emptyArray(),
                                                           defaultArgs: Bundle? = null): T {
    return ViewModelProvider(requireActivity(), MultipleParametrizedVMSavedStateFactory(constructorParams, this, defaultArgs)).get()
}


inline fun <reified T : AndroidViewModel> Fragment.sharedProviderAVM(param: Any,
                                                                     defaultArgs: Bundle? = null): T {
    return ViewModelProvider(requireActivity(), ParametrizedAVMSavedStateFactory(requireActivity().application, param, this, defaultArgs)).get()
}

inline fun <reified T : AndroidViewModel> Fragment.sharedProviderAVM(constructorParams: Array<out Any> = emptyArray(),
                                                                     defaultArgs: Bundle? = null): T {
    return ViewModelProvider(requireActivity(), MultipleParametrizedAVMSavedStateFactory(constructorParams, requireActivity().application, this, defaultArgs)).get()
}


inline fun <reified T : ViewModel> Fragment.sharedProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProvider(requireActivity(), factory).get()
}