package com.crazylegend.kotlinextensions.livedata

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

//app compat activity
inline fun <reified T : ViewModel> AppCompatActivity.compatProvider(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : AndroidViewModel> AppCompatActivity.compatProvider(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory).get(T::class.java)
}


//fragment
inline fun <reified T : ViewModel> Fragment.fragmentProvider(): T {

    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : AndroidViewModel> Fragment.fragmentProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(this, factory).get(T::class.java)
}

//fragment activity
inline fun <reified T : ViewModel> FragmentActivity.fragmentActivityProvider(): T {

    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : AndroidViewModel> FragmentActivity.fragmentActivityProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(this, factory).get(T::class.java)
}


//shared model
inline fun <reified T : ViewModel> Fragment.sharedProvider(): T {

    return ViewModelProviders.of(requireActivity()).get(T::class.java)
}

inline fun <reified T : AndroidViewModel> Fragment.sharedProvider(factory: ViewModelProvider.Factory): T {

    return ViewModelProviders.of(requireActivity(), factory).get(T::class.java)
}



