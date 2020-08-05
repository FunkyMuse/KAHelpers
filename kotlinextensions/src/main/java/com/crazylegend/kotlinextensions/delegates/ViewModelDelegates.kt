package com.crazylegend.kotlinextensions.delegates

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.kotlinextensions.livedata.*


/**
 * Created by crazy on 11/14/19 to long live and prosper !
 */

//activity
inline fun <reified VM : ViewModel> AppCompatActivity.activityVM() = lazy {
    compatProvider<VM>()
}


inline fun <reified VM : ViewModel> AppCompatActivity.activityVM(param: Any) = lazy {
    compatProvider<VM>(param)
}


inline fun <reified VM : ViewModel> AppCompatActivity.activityVM(constructorParams: Array<out Any>) = lazy {
    compatProvider<VM>(constructorParams)
}

inline fun <reified VM : AndroidViewModel> AppCompatActivity.activityAVM(param: Any) = lazy {
    compatProviderAVM<VM>(param)
}


inline fun <reified VM : AndroidViewModel> AppCompatActivity.activityAVM(constructorParams: Array<out Any>) = lazy {
    compatProviderAVM<VM>(constructorParams)
}

inline fun <reified VM : ViewModel> AppCompatActivity.activityVM(factory: ViewModelProvider.Factory) = lazy {
    compatProvider<VM>(factory)
}


//fragment

inline fun <reified VM : ViewModel> Fragment.fragmentVM() = lazy {
    fragmentProvider<VM>()
}


inline fun <reified VM : ViewModel> Fragment.fragmentVM(param: Any) = lazy {
    fragmentProvider<VM>(param)
}


inline fun <reified VM : ViewModel> Fragment.fragmentVM(constructorParams: Array<out Any>) = lazy {
    fragmentProvider<VM>(constructorParams)
}


inline fun <reified VM : AndroidViewModel> Fragment.fragmentAVM(param: Any) = lazy {
    fragmentProviderAVM<VM>(param)
}


inline fun <reified VM : AndroidViewModel> Fragment.fragmentAVM(constructorParams: Array<out Any>) = lazy {
    fragmentProviderAVM<VM>(constructorParams)
}


inline fun <reified VM : ViewModel> Fragment.fragmentVM(factory: ViewModelProvider.Factory) = lazy {
    fragmentProvider<VM>(factory)
}

//shared
inline fun <reified VM : ViewModel> Fragment.sharedVM() = lazy {
    sharedProvider<VM>()
}

inline fun <reified VM : ViewModel> Fragment.sharedVM(factory: ViewModelProvider.Factory) = lazy {
    sharedProvider<VM>(factory)
}


inline fun <reified VM : ViewModel> Fragment.sharedVM(param: Any) = lazy {
    sharedProvider<VM>(param)
}


inline fun <reified VM : ViewModel> Fragment.sharedVM(constructorParams: Array<out Any>) = lazy {
    sharedProvider<VM>(constructorParams)
}


inline fun <reified VM : AndroidViewModel> Fragment.sharedAVM(param: Any) = lazy {
    sharedProviderAVM<VM>(param)
}


inline fun <reified VM : AndroidViewModel> Fragment.sharedAVM(constructorParams: Array<out Any>) = lazy {
    sharedProviderAVM<VM>(constructorParams)
}