package com.crazylegend.kotlinextensions.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch


/**
 * Created by crazy on 3/18/20 to long live and prosper !
 */


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelIOCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(ioDispatcher) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelMainCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(mainDispatcher) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelDefaultCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(defaultDispatcher) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelUnconfinedCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(unconfinedDispatcher) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelNonCancellableCoroutine(action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(NonCancellable) {
        action(this)
    }
}

