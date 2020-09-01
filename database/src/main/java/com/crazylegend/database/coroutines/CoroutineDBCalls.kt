package com.crazylegend.database.coroutines

import androidx.lifecycle.*
import com.crazylegend.coroutines.*
import com.crazylegend.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext


/**
 * Created by hristijan on 10/25/19 to long live and prosper !
 */


fun <T> ViewModel.makeDBCallLiveData(apiCall: suspend () -> T): LiveData<DBResult<T>> {
    return liveData(viewModelScope.coroutineContext) {
        emit(DBResult.Querying)
        try {
            subscribeDBCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
    }
}


fun <T> ViewModel.makeDBCallLiveData(mediatorLiveData: MediatorLiveData<DBResult<T>>, apiCall: suspend () -> T) {
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
fun <T> CoroutineContext.makeDBCallLiveData(apiCall: suspend () -> T): LiveData<DBResult<T>> {
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
fun <T> CoroutineContext.makeDBCallLiveData(mediatorLiveData: MediatorLiveData<DBResult<T>>, apiCall: suspend () -> T) {
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
        crossinline onFlow: Flow<T>?.() -> Unit = {},
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {

            val result = dbCall()
            result.onFlow()
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
                dbResult.subscribePost(it, includeEmptyData)
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
        crossinline onFlow: Flow<T>?.() -> Unit = {},
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow.onFlow()
            flow?.collect {
                dbResult.subscribePost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


inline fun <T> ViewModel.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        crossinline onFlow: Flow<T>?.() -> Unit = {},
        crossinline dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow.onFlow()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> CoroutineScope.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
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


fun <T> AndroidViewModel.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
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


fun <T> AndroidViewModel.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)
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
                dbResult.subscribe(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


suspend fun <T> dbCallList(
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): DBResult<T> {

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

suspend fun <T> dbCall(dbCall: suspend () -> T?): DBResult<T> {
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


