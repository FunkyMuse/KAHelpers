package com.crazylegend.retrofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.defaultDispatcher
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.coroutines.unconfinedDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by crazy on 1/7/21 to long live and prosper !
 */

inline fun ViewModel.viewModelSupervisorIOJob(parentJob: Job? = null, crossinline function: suspend () -> Unit) {
    viewModelScope.launch(ioDispatcher + SupervisorJob(parentJob)) {
        function()
    }
}

inline fun ViewModel.viewModelSupervisorDefaultJob(parentJob: Job? = null, crossinline function: suspend () -> Unit) {
    viewModelScope.launch(defaultDispatcher + SupervisorJob(parentJob)) {
        function()
    }
}

inline fun ViewModel.viewModelSupervisorUnconfinedJob(parentJob: Job? = null, crossinline function: suspend () -> Unit) {
    viewModelScope.launch(unconfinedDispatcher + SupervisorJob(parentJob)) {
        function()
    }
}

inline fun ViewModel.viewModelSupervisorMainJob(parentJob: Job? = null, crossinline function: suspend () -> Unit) {
    viewModelScope.launch(mainDispatcher + SupervisorJob(parentJob)) {
        function()
    }
}

