package com.crazylegend.kotlinextensions.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by crazy on 3/17/20 to long live and prosper !
 */


inline fun <reified T : ViewModel> viewModelFactory(crossinline viewModel: () -> T) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModel() as T
}


inline fun <reified T : AndroidViewModel> androidViewModelFactory(application: Application, crossinline viewModel: () -> T) = object :
        ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModel() as T
}

