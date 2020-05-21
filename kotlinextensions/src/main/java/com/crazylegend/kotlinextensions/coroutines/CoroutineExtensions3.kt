package com.crazylegend.kotlinextensions.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelIOCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(ioDispatcher, coroutineStart) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelMainCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(mainDispatcher, coroutineStart) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelDefaultCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(defaultDispatcher, coroutineStart) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelUnconfinedCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(unconfinedDispatcher, coroutineStart) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelNonCancellableCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT,action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(NonCancellable, coroutineStart) {
        action(this)
    }
}


inline fun CoroutineScope.main(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(mainDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.io(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(ioDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.default(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(defaultDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.unconfined(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(unconfinedDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.nonCancellable(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(NonCancellable, coroutineStart) {
        function()
    }
}
