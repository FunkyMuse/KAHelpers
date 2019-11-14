package com.crazylegend.kotlinextensions.delegates

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.livedata.fragmentVM


/**
 * Created by crazy on 11/14/19 to long live and prosper !
 */


inline fun <reified VM:ViewModel> Fragment.fragmentVM() = lazy {
    fragmentVM<VM>()
}

inline fun <reified VM:ViewModel> AppCompatActivity.activityVM() = lazy {
    compatProvider<VM>()
}

inline fun <reified VM:ViewModel> Fragment.fragmentVM(factory: ViewModelProvider.Factory) = lazy {
    fragmentVM<VM>(factory)
}

inline fun <reified VM:ViewModel> AppCompatActivity.activityVM(factory: ViewModelProvider.Factory) = lazy {
    compatProvider<VM>(factory)
}

