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


fun <T> CoroutineScope.apiCallSharedFlow(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                         replay: Int = 0,
                                         apiCall: suspend () -> Response<T>): SharedFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.shareIn(this, sharing, replay)


suspend fun <T> CoroutineScope.apiCallSharedFlowInScope(sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                                        replay: Int = 0, apiCall: suspend () -> Response<T>): SharedFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.shareIn(this, sharing, replay)


suspend fun <T> apiCallSharedFlowWithinScope(coroutineScope: CoroutineScope,
                                             sharing: SharingStarted = SharingStarted.WhileSubscribed(),
                                             replay: Int = 0,
                                             apiCall: suspend () -> Response<T>): SharedFlow<RetrofitResult<T>> =
        flow {
            try {
                emit(retrofitSubscribe(apiCall.invoke()))
            } catch (t: Throwable) {
                emit(RetrofitResult.Error(t))
            }
        }.onStart {
            emit(RetrofitResult.Loading)
        }.shareIn(coroutineScope, sharing, replay)


fun <T> CoroutineScope.makeApiCallList(
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>): Job {
    return launch(ioDispatcher) {
        retrofitResult.loading()
        try {
            retrofitResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>): Job {
    return launch(ioDispatcher) {
        retrofitResult.loading()

        try {
            retrofitResult.subscribe(apiCall())
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallList(
        response: Response<T>,
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    return launch(ioDispatcher) {
        retrofitResult.loading()

        try {
            retrofitResult.subscribeList(response, includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCallList(
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>): Job {
    return viewModelIOCoroutine {
        retrofitResult.loading()

        try {
            retrofitResult.subscribeList(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}

fun <T> CoroutineScope.makeApiCall(
        response: Response<T>,
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>
): Job {
    return launch(ioDispatcher) {
        retrofitResult.loading()

        try {
            retrofitResult.subscribe(response)
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }

}

fun <T> ViewModel.makeApiCall(
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>): Job {

    return viewModelIOCoroutine {
        retrofitResult.loading()
        try {
            retrofitResult.subscribe(apiCall())
        } catch (t: Throwable) {
            retrofitResult.callError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCallAsync(
        retrofitResult: MutableSharedFlow<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>): Job {

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

inline fun <T> ViewModel.makeApiCallSharedFlow(sharedFlow: MutableSharedFlow<RetrofitResult<T>>, crossinline apiCall: suspend () -> Response<T>) {
    viewModelScope.launch {
        supervisorScope {
            sharedFlow.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                sharedFlow.subscribe(task.await())
            } catch (t: Throwable) {
                sharedFlow.callError(t)
            }
        }
    }
}

inline fun <T> ViewModel.makeApiCallListSharedFlow(sharedFlow: MutableSharedFlow<RetrofitResult<T>>,
                                                   includeEmptyData: Boolean = false,
                                                   crossinline apiCall: suspend () -> Response<T>) {
    viewModelScope.launch {
        supervisorScope {
            sharedFlow.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                sharedFlow.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                sharedFlow.callError(t)
            }
        }
    }
}

inline fun <T> CoroutineScope.makeApiCallSharedFlow(sharedFlow: MutableSharedFlow<RetrofitResult<T>>, crossinline apiCall: suspend () -> Response<T>) {

    launch {
        supervisorScope {
            sharedFlow.loading()

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                sharedFlow.subscribe(task.await())
            } catch (t: Throwable) {
                sharedFlow.callError(t)
            }
        }
    }
}

inline fun <T> CoroutineScope.makeApiCallListSharedFlow(sharedFlow: MutableSharedFlow<RetrofitResult<T>>,
                                                        includeEmptyData: Boolean = false,
                                                        crossinline apiCall: suspend () -> Response<T>) {
    launch {
        supervisorScope {
            sharedFlow.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                sharedFlow.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                sharedFlow.callError(t)
            }
        }
    }
}
