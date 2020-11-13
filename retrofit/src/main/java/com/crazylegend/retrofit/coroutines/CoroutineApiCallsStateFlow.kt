package com.crazylegend.retrofit.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.coroutines.viewModelIOCoroutine
import com.crazylegend.retrofit.retrofitResult.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * Created by crazy on 11/12/20 to long live and prosper !
 */


fun <T> CoroutineScope.apiCallStateFlow(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                        initialValue: RetrofitResult<T> = RetrofitResult.Loading,
                                        apiCall: suspend () -> Response<T>?): StateFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.stateIn(this, sharing, initialValue)


suspend fun <T> CoroutineScope.apiCallStateFlowInScope(apiCall: suspend () -> Response<T>?): StateFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.stateIn(this)


suspend fun <T> apiCallStateFlowWithinScope(coroutineScope: CoroutineScope, apiCall: suspend () -> Response<T>?): StateFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.stateIn(coroutineScope)


fun <T> CoroutineScope.makeApiCallList(
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {

    retrofitResult.loading()

    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loading()
    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribe(apiCall())
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallList(
        response: Response<T>?,
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    retrofitResult.loading()
    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribeList(response, includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCallList(
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loading()
    return viewModelIOCoroutine {
        try {
            retrofitResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        response: Response<T>?,
        retrofitResult: MutableStateFlow<RetrofitResult<T>>
): Job {
    retrofitResult.loading()
    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribe(response)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCall(
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loading()
    return viewModelIOCoroutine {
        try {
            retrofitResult.subscribe(apiCall())
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallAsync(
        retrofitResult: MutableStateFlow<RetrofitResult<T>>,
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

inline fun <T : RetrofitResult<T>> ViewModel.makeApiCallStateFlow(stateFlow: MutableStateFlow<RetrofitResult<T>>, crossinline apiCall: suspend () -> Response<T>?) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = retrofitLoading
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                stateFlow.value = retrofitSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = retrofitCallError(t)
            }
        }
    }
}

inline fun <T : RetrofitResult<T>> ViewModel.makeApiCallListStateFlow(stateFlow: MutableStateFlow<RetrofitResult<T>>,
                                                                      includeEmptyData: Boolean = false,
                                                                      crossinline apiCall: suspend () -> Response<T>?) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = retrofitLoading
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                stateFlow.value = retrofitSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = retrofitCallError(t)
            }
        }
    }
}

inline fun <T : RetrofitResult<T>> CoroutineScope.makeApiCallStateFlow(stateFlow: MutableStateFlow<RetrofitResult<T>>, crossinline apiCall: suspend () -> Response<T>?) {

    launch {
        supervisorScope {
            stateFlow.value = retrofitLoading

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                stateFlow.value = retrofitSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = retrofitCallError(t)
            }
        }
    }
}

inline fun <T : RetrofitResult<T>> CoroutineScope.makeApiCallListStateFlow(stateFlow: MutableStateFlow<RetrofitResult<T>>,
                                                                           includeEmptyData: Boolean = false,
                                                                           crossinline apiCall: suspend () -> Response<T>?) {
    launch {
        supervisorScope {
            stateFlow.value = retrofitLoading
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                stateFlow.value = retrofitSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = retrofitCallError(t)
            }
        }
    }
}
