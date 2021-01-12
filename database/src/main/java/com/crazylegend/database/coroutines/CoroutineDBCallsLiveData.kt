package com.crazylegend.database.coroutines

import androidx.lifecycle.*
import com.crazylegend.coroutines.*
import com.crazylegend.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.CoroutineContext


/**
 * Created by hristijan on 10/25/19 to long live and prosper !
 */


inline fun <T> ViewModel.makeDBCallLiveData(crossinline apiCall: suspend () -> T): LiveData<DBResult<T>> {
    return liveData(viewModelScope.coroutineContext) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }
}


inline fun <T> ViewModel.makeDBCallLiveData(mediatorLiveData: MediatorLiveData<DBResult<T>>, crossinline apiCall: suspend () -> T) {
    val ld: LiveData<DBResult<T>> = liveData(viewModelScope.coroutineContext) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}


suspend fun <T> LiveDataScope<DBResult<T>>.subscribeDBCall(res: T) {
    emit(DBResult.Success(res))
}


/**
 *
 * @receiver CoroutineContext
 * @param apiCall SuspendFunction0<T>
 * @return LiveData<DBResult<T>>
 */
inline fun <T> CoroutineContext.makeDBCallLiveData(crossinline apiCall: suspend () -> T): LiveData<DBResult<T>> {
    return liveData(this) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param mediatorLiveData MediatorLiveData<DBResult<T>>
 * @param apiCall SuspendFunction0<T>
 */
inline fun <T> CoroutineContext.makeDBCallLiveData(mediatorLiveData: MediatorLiveData<DBResult<T>>, crossinline apiCall: suspend () -> T) {
    val ld: LiveData<DBResult<T>> = liveData(this) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}


//flow

inline fun <T> CoroutineScope.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {

            val result = dbCall()
            result?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

inline fun <T> CoroutineScope.makeDBCallFlow(
        queryModel: Flow<T>?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        crossinline onFlow: Flow<T>?.() -> Unit = {}

): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            queryModel.onFlow()
            queryModel?.collect {
                dbResult.subscribePost(it)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

inline fun <T> CoroutineScope.makeDBCallListFlow(
        queryModel: Flow<T>?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline onFlow: Flow<T>?.() -> Unit = {}
): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            queryModel.onFlow()
            queryModel?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }

}


inline fun <T> ViewModel.makeDBCallFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribePost(it)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


inline fun <T> ViewModel.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


inline fun <T> CoroutineScope.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        crossinline dbCall: suspend () -> T?): Job {
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


inline fun <T> CoroutineScope.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline dbCall: suspend () -> T?): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


inline fun <T> ViewModel.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline dbCall: suspend () -> T?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


inline fun <T> ViewModel.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        crossinline dbCall: suspend () -> T?): Job {
    return viewModelScope.launch(mainDispatcher) {
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


fun <T> CoroutineScope.makeDBCallListAsync(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()

            try {
                val task = async(ioDispatcher) {
                    queryModel
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCallAsync(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()

            try {
                val task = async(ioDispatcher) {
                    queryModel
                }
                dbResult.subscribe(task.await())
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


suspend inline fun <T> dbCallList(
        includeEmptyData: Boolean = false,
        crossinline dbCall: suspend () -> T?): DBResult<T> {

    return withIOContext {
        try {
            databaseSubscribeList(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            withMainContext {
                databaseError(t)
            }
        }
    }
}

suspend inline fun <T> dbCall(crossinline dbCall: suspend () -> T?): DBResult<T> {
    return withIOContext {
        try {
            databaseSubscribe(dbCall())
        } catch (t: Throwable) {
            withMainContext {
                databaseError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCall(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false
): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribePost(queryModel)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallList(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    dbResult.queryingPost()
    return launch(ioDispatcher) {
        try {
            dbResult.subscribeListPost(queryModel, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }

}


fun <T> ViewModel.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribePost(dbCall())
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> ViewModel.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun ViewModel.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}

fun ViewModel.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribePost(dbCall())
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}


fun <T> CoroutineScope.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}


fun CoroutineScope.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return launch(ioDispatcher) {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}

fun CoroutineScope.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return launch(ioDispatcher) {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
            launch(mainDispatcher) {
                onErrorAction(t)
            }
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}


// no wrappers getting the result straight up
fun <T> ViewModel.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            val call = dbCall()
            viewModelMainCoroutine {
                onCalled(call)
            }
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {
    return launch(ioDispatcher) {
        try {
            val call = dbCall()
            launch(mainDispatcher) {
                onCalled(call)
            }
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}


// no wrappers getting the result straight up for flow

fun <T> ViewModel.makeDBCallFlow(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Flow<T>,
        onCalled: (model: T) -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            val call = dbCall()
            call.collect { model ->
                withMainContext {
                    onCalled(model)
                }
            }

        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            withMainContext {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCallFlow(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Flow<T>,
        onCalled: (model: T) -> Unit): Job {
    return launch(ioDispatcher) {
        try {
            val call = dbCall()
            call.collect {
                withMainContext {
                    onCalled(it)
                }
            }

        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}

fun <T> dbCallAsFlow(apiCall: suspend () -> T?): Flow<DBResult<T>> =
        flow {
            try {
                emit(databaseSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(databaseError(t))
            }
        }.onStart {
            emit(databaseQuerying)
        }