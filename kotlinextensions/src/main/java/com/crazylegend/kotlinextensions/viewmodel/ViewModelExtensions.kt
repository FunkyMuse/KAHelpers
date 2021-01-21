package com.crazylegend.kotlinextensions.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */


//app compat activity
inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(): T {
    return ViewModelProvider(this).get()
}

inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get()
}

//fragment
inline fun <reified T : ViewModel> Fragment.fragmentProvider(): T {

    return ViewModelProvider(this).get()
}


inline fun <reified T : ViewModel> Fragment.fragmentProvider(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get()
}

//shared model from the same activity
inline fun <reified T : ViewModel> Fragment.sharedProvider(): T {

    return ViewModelProvider(requireActivity()).get()
}

inline fun <reified T : ViewModel> Fragment.sharedProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProvider(requireActivity(), factory).get()
}