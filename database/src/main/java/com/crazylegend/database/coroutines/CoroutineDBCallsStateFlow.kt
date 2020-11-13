package com.crazylegend.database.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.coroutines.viewModelIOCoroutine
import com.crazylegend.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */

inline fun <T : DBResult<T>> ViewModel.makeDBCallStateFlow(stateFlow: MutableStateFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> ViewModel.makeDBCallListStateFlow(stateFlow: MutableStateFlow<DBResult<T>>,
                                                               includeEmptyData: Boolean = false,
                                                               crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> CoroutineScope.makeDBCallStateFlow(stateFlow: MutableStateFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {

    launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying

            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> CoroutineScope.makeDBCallListStateFlow(stateFlow: MutableStateFlow<DBResult<T>>,
                                                                    includeEmptyData: Boolean = false,
                                                                    crossinline dbCall: suspend () -> T) {
    launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}


fun <T> CoroutineScope.apiCallStateFlow(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                        initialValue: DBResult<T> = databaseQuerying,
                                        apiCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(this, sharing, initialValue)


suspend fun <T> CoroutineScope.apiCallStateFlowInScope(apiCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(this)


suspend fun <T> apiCallStateFlowWithinScope(coroutineScope: CoroutineScope, apiCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(coroutineScope)


fun <T> CoroutineScope.makeApiCallList(
        dbResult: MutableStateFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> T?): Job {

    dbResult.querying()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        dbResult: MutableStateFlow<DBResult<T>>,
        apiCall: suspend () -> T?): Job {
    dbResult.querying()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribe(apiCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallList(
        response: T?,
        dbResult: MutableStateFlow<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    dbResult.querying()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribeList(response, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCallList(
        dbResult: MutableStateFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> T?): Job {
    dbResult.querying()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        response: T?,
        dbResult: MutableStateFlow<DBResult<T>>
): Job {
    dbResult.querying()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribe(response)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCall(
        dbResult: MutableStateFlow<DBResult<T>>,
        apiCall: suspend () -> T?): Job {
    dbResult.querying()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribe(apiCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallAsync(
        dbResult: MutableStateFlow<DBResult<T>>,
        apiCall: suspend () -> T?): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}