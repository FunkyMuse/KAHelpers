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


fun <T> CoroutineScope.dbCallStateFlow(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                       initialValue: DBResult<T> = databaseQuerying,
                                       dbCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(this, sharing, initialValue)


suspend fun <T> CoroutineScope.dbCallStateFlowInScope(dbCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(this)


suspend fun <T> dbCallStateFlowWithinScope(coroutineScope: CoroutineScope, dbCall: suspend () -> T?): StateFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.stateIn(coroutineScope)


fun <T> CoroutineScope.makeDBCallList(
        dbResult: MutableStateFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {

    dbResult.querying()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
        dbResult: MutableStateFlow<DBResult<T>>,
        dbCall: suspend () -> T?): Job {
    dbResult.querying()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribe(dbCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallList(
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

fun <T> ViewModel.makeDBCallList(
        dbResult: MutableStateFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.querying()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
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

fun <T> ViewModel.makeDBCall(
        dbResult: MutableStateFlow<DBResult<T>>,
        dbCall: suspend () -> T?): Job {
    dbResult.querying()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribe(dbCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallAsync(
        dbResult: MutableStateFlow<DBResult<T>>,
        dbCall: suspend () -> T?): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}