package com.crazylegend.coroutines

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

fun <T> AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}


fun AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}



fun CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }

    }
}


fun CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}