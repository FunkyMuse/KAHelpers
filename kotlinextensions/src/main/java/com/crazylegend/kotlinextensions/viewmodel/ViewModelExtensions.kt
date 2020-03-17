package com.crazylegend.kotlinextensions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by crazy on 3/17/20 to long live and prosper !
 */


inline fun <T> viewModelFactory() {
    TODO()
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            TODO("Not yet implemented")
        }
    }
}