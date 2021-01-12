package com.crazylegend.retrofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.defaultDispatcher
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.coroutines.unconfinedDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Created by crazy on 1/7/21 to long live and prosper !
 */

inline fun ViewModel.viewModelIOSupervised(crossinline function: suspend () -> Unit) {
    viewModelScope.launch(ioDispatcher) {
        supervisorScope {
            function()
        }
    }
}

inline fun ViewModel.viewModelSupervised(crossinline function: suspend () -> Unit) {
    viewModelScope.launch {
        supervisorScope {
            function()
        }
    }
}

inline fun ViewModel.viewModelDefaultSupervised(crossinline function: suspend () -> Unit) {
    viewModelScope.launch(defaultDispatcher) {
        supervisorScope {
            function()
        }
    }
}

inline fun ViewModel.viewModelUnconfinedSupervised(crossinline function: suspend () -> Unit) {
    viewModelScope.launch(unconfinedDispatcher) {
        supervisorScope {
            function()
        }
    }
}

inline fun ViewModel.viewModelMainSupervised(crossinline function: suspend () -> Unit) {
    viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            function()
        }
    }
}