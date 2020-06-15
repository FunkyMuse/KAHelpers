package com.crazylegend.kotlinextensions.coroutines

import androidx.lifecycle.*
import com.crazylegend.kotlinextensions.databaseResult.DBResult
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.RetrofitResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


/**
 * Created by hristijan on 10/25/19 to long live and prosper !
 */

/**
 * In your view model call the function and receive a wrapped response with [RetrofitResult]

val posts = makeApiCallLiveDataAsync {
retrofit?.getPosts()
}

 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>?
 */
fun <T> ViewModel.makeApiCallLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
}

/**
 * Makes the api call with mediator, saves few lines of code
 * @receiver ViewModel
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> ViewModel.makeApiCallLiveData(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }

    return ld

}

/**
 *
 * @receiver ViewModel
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> ViewModel.makeApiCallLiveDataAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCall(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
}


/**
 *
 * @receiver ViewModel
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> ViewModel.makeApiCallLiveDataAsync(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCall(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}

/**
 *
 * @receiver ViewModel
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> ViewModel.makeApiCallLiveDataListAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCallList(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
}

/**
 *
 * @receiver ViewModel
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> ViewModel.makeApiCallLiveDataListAsync(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCallList(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}

/**
 *
 * @receiver ViewModel
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> ViewModel.makeApiCallListLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCallList(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
}

/**
 *
 * @receiver ViewModel
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> ViewModel.makeApiCallListLiveData(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = viewModelScope.coroutineContext) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCallList(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }

}


suspend fun <T> LiveDataScope<RetrofitResult<T>>.subscribeApiCall(res: Response<T>?) {
    if (res == null) {
        emit(RetrofitResult.EmptyData)
    } else {
        if (res.isSuccessful) {
            val body = res.body()
            if (body == null) {
                emit(RetrofitResult.EmptyData)
            } else {
                emit(RetrofitResult.Success(body))
            }
        } else {
            emit(RetrofitResult.ApiError(res.code(), res.errorBody()))
        }
    }
}

suspend fun <T> LiveDataScope<RetrofitResult<T>>.subscribeApiCallList(res: Response<T>?) {
    if (res == null) {
        emit(RetrofitResult.EmptyData)
    } else {
        if (res.isSuccessful) {
            val body = res.body()
            if (body == null) {
                emit(RetrofitResult.EmptyData)
            } else {
                if (body is List<*>) {
                    val list = body as List<*>
                    if (list.isNullOrEmpty()) {
                        emit(RetrofitResult.EmptyData)
                    } else {
                        emit(RetrofitResult.Success(body))
                    }
                } else {
                    emit(RetrofitResult.Success(body))
                }
            }
        } else {
            emit(RetrofitResult.ApiError(res.code(), res.errorBody()))
        }
    }
}


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
 * Cancel the Job if it's active.
 */
fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}


/**
 *
 * @receiver CoroutineContext
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> CoroutineContext.makeApiCallLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = this) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> CoroutineContext.makeApiCallLiveData(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = this) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCall(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }

    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}


fun <T> CoroutineContext.makeApiCallLiveDataAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = this) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCall(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> CoroutineContext.makeApiCallLiveDataAsync(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = this) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCall(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> CoroutineContext.makeApiCallLiveDataListAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = this) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCallList(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> CoroutineContext.makeApiCallLiveDataListAsync(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = this) {
        emit(RetrofitResult.Loading)
        supervisorScope {
            try {
                val res = async {
                    apiCall.invoke()
                }
                subscribeApiCallList(res.await())
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }
    }
    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
}


/**
 *
 * @receiver CoroutineContext
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return LiveData<RetrofitResult<T>>
 */
fun <T> CoroutineContext.makeApiCallListLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>> {
    return liveData(context = this) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCallList(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
}

/**
 *
 * @receiver CoroutineContext
 * @param mediatorLiveData MediatorLiveData<RetrofitResult<T>>
 * @param apiCall SuspendFunction0<Response<T>?>
 */
fun <T> CoroutineContext.makeApiCallListLiveData(mediatorLiveData: MediatorLiveData<RetrofitResult<T>>, apiCall: suspend () -> Response<T>?) {
    val ld: LiveData<RetrofitResult<T>> = liveData(context = this) {
        emit(RetrofitResult.Loading)
        try {
            subscribeApiCallList(apiCall.invoke())
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
    mediatorLiveData.addSource(ld) {
        mediatorLiveData.postValue(it)
    }
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
