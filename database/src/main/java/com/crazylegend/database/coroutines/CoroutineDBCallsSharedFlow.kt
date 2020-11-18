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

inline fun <T> ViewModel.makeDBCallSharedFlow(sharedFlow: MutableSharedFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            sharedFlow.emit(DBResult.Querying)
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                sharedFlow.emit(databaseSubscribe(task.await()))
            } catch (t: Throwable) {
                sharedFlow.emit(databaseError(t))
            }
        }
    }
}

inline fun <T> ViewModel.makeDBCallListSharedFlow(sharedFlow: MutableSharedFlow<DBResult<T>>,
                                                  includeEmptyData: Boolean = false,
                                                  crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            sharedFlow.emit(DBResult.Querying)
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                sharedFlow.emit(databaseSubscribeList(task.await(), includeEmptyData))
            } catch (t: Throwable) {
                sharedFlow.emit(databaseError(t))
            }
        }
    }
}

inline fun <T> CoroutineScope.makeDBCallSharedFlow(sharedFlow: MutableSharedFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {

    launch {
        supervisorScope {
            sharedFlow.emit(DBResult.Querying)

            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                sharedFlow.emit(databaseSubscribe(task.await()))
            } catch (t: Throwable) {
                sharedFlow.emit(databaseError(t))
            }
        }
    }
}

inline fun <T> CoroutineScope.makeDBCallListSharedFlow(sharedFlow: MutableSharedFlow<DBResult<T>>,
                                                       includeEmptyData: Boolean = false,
                                                       crossinline dbCall: suspend () -> T) {
    launch {
        supervisorScope {
            sharedFlow.emit(DBResult.Querying)
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                sharedFlow.emit(databaseSubscribeList(task.await(), includeEmptyData))
            } catch (t: Throwable) {
                sharedFlow.emit(databaseError(t))
            }
        }
    }
}


fun <T> CoroutineScope.dbCallSharedFlow(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                        replay: Int = 0,
                                        dbCall: suspend () -> T?): SharedFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.shareIn(this, sharing, replay)


suspend fun <T> CoroutineScope.dbCallSharedFlowInScope(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                                       replay: Int = 0, dbCall: suspend () -> T?): SharedFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.shareIn(this, sharing, replay)


suspend fun <T> dbCallSharedFlowWithinScope(coroutineScope: CoroutineScope,
                                            sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                            replay: Int = 0,
                                            dbCall: suspend () -> T?): SharedFlow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(dbCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }.shareIn(coroutineScope, sharing, replay)


fun <T> CoroutineScope.makeDBCallList(
        dbResult: MutableSharedFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {

    return launch(ioDispatcher) {
        dbResult.querying()

        try {
            dbResult.subscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
        dbResult: MutableSharedFlow<DBResult<T>>,
        dbCall: suspend () -> T?): Job {

    return launch(ioDispatcher) {
        dbResult.querying()
        try {
            dbResult.subscribe(dbCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallList(
        response: T?,
        dbResult: MutableSharedFlow<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {

    return launch(ioDispatcher) {
        dbResult.querying()
        try {
            dbResult.subscribeList(response, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeDBCallList(
        dbResult: MutableSharedFlow<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    return viewModelIOCoroutine {
        dbResult.querying()

        try {
            dbResult.subscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
        response: T?,
        dbResult: MutableSharedFlow<DBResult<T>>
): Job {

    return launch(ioDispatcher) {
        dbResult.querying()
        try {
            dbResult.subscribe(response)
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeDBCall(
        dbResult: MutableSharedFlow<DBResult<T>>,
        dbCall: suspend () -> T?): Job {
    return viewModelIOCoroutine {
        dbResult.querying()
        try {
            dbResult.subscribe(dbCall())
        } catch (t: Throwable) {
            dbResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallAsync(
        dbResult: MutableSharedFlow<DBResult<T>>,
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