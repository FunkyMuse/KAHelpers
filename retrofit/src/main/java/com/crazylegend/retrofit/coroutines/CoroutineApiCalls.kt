package com.crazylegend.retrofit.coroutines

import androidx.lifecycle.*
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.coroutines.withIOContext
import com.crazylegend.coroutines.withMainContext
import com.crazylegend.retrofit.retrofitResult.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
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

fun <T> AndroidViewModel.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}

fun <T> AndroidViewModel.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeApiCallListAsync(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    response
                }
                retrofitResult.subscribeListPost(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeApiCallAsync(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    response
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
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


fun <T> AndroidViewModel.makeApiCallAsync(apiCall: suspend () -> Response<T>?,
                                          onError: (throwable: Throwable) -> Unit = { _ -> },
                                          onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                          onResponse: (response: T?) -> Unit
): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                val response = task.await()
                response?.apply {
                    if (isSuccessful) {
                        onResponse(body())
                    } else {
                        onUnsuccessfulCall(errorBody(), code())
                    }
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }

    }
}


fun <T> CoroutineScope.makeApiCallAsync(apiCall: suspend () -> Response<T>?,
                                        onError: (throwable: Throwable) -> Unit = { _ -> },
                                        onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                        onResponse: (response: T?) -> Unit
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                val response = task.await()
                response?.apply {
                    if (isSuccessful) {
                        onResponse(body())
                    } else {
                        onUnsuccessfulCall(errorBody(), code())
                    }
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }

    }
}


fun <T> CoroutineScope.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {


    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }

        }
    }
}

fun <T> CoroutineScope.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()

    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }

}


suspend fun <T> apiCallList(
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): RetrofitResult<T> {

    return withIOContext {
        try {
            retrofitSubscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            withMainContext {
                retrofitCallError(t)
            }
        }
    }
}

suspend fun <T> apiCall(
        apiCall: suspend () -> Response<T>?): RetrofitResult<T> {
    return withIOContext {
        try {
            retrofitSubscribe(apiCall())
        } catch (t: Throwable) {
            withMainContext {
                retrofitCallError(t)
            }
        }
    }
}
