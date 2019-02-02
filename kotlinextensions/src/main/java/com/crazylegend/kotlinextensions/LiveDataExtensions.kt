package com.crazylegend.kotlinextensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

//app compat activity
inline fun <reified T : ViewModel> AppCompatActivity.provider(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> AppCompatActivity.provider(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}


//fragment
inline fun <reified T : ViewModel> Fragment.provider(): T {

    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.provider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(this, factory).get(T::class.java)
}

//fragment activity
inline fun <reified T : ViewModel> FragmentActivity.provider(): T {

    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.provider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(this, factory).get(T::class.java)
}


//shared model
inline fun <reified T : ViewModel> Fragment.sharedProvider(): T {

    return ViewModelProviders.of(requireActivity()).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.sharedProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(requireActivity(), factory).get(T::class.java)
}



