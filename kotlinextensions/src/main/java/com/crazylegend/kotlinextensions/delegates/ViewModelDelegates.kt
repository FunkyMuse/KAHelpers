package com.crazylegend.kotlinextensions.delegates

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.livedata.fragmentProvider
import com.crazylegend.kotlinextensions.livedata.sharedProvider


/**
 * Created by crazy on 11/14/19 to long live and prosper !
 */


inline fun <reified VM:ViewModel> Fragment.fragmentVM() = lazy {
    fragmentProvider<VM>()
}

inline fun <reified VM:ViewModel> AppCompatActivity.activityVM() = lazy {
    compatProvider<VM>()
}

inline fun <reified VM:ViewModel> Fragment.fragmentVM(factory: ViewModelProvider.Factory) = lazy {
    fragmentProvider<VM>(factory)
}

inline fun <reified VM:ViewModel> AppCompatActivity.activityVM(factory: ViewModelProvider.Factory) = lazy {
    compatProvider<VM>(factory)
}

inline fun <reified VM : ViewModel> Fragment.sharedVM() = lazy {
    sharedProvider<VM>()
}

inline fun <reified VM : ViewModel> Fragment.sharedVM(factory: ViewModelProvider.Factory) = lazy {
    sharedProvider<VM>(factory)
}