package com.crazylegend.kotlinextensions.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import com.crazylegend.kotlinextensions.database.DBResult
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import retrofit2.Response


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
fun <T> makeApiCallLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>>? {
    return liveData {
        emit(RetrofitResult.Loading)
        try {
            val res = apiCall.invoke()
            subscribeApiCall(res)
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
    }
}

fun <T> makeApiCallLiveDataAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>>? {
    return liveData {
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

fun <T> makeApiCallLiveDataListAsync(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>>? {
    return liveData {
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

fun <T> makeApiCallListLiveData(apiCall: suspend () -> Response<T>?): LiveData<RetrofitResult<T>>? {
    return liveData {
        emit(RetrofitResult.Loading)
        try {
            val res = apiCall.invoke()
            subscribeApiCallList(res)
        } catch (t: Throwable) {
            emit(RetrofitResult.Error(t))
        }
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


fun <T> makeDBCallLiveData(apiCall: suspend () -> T): LiveData<DBResult<T>>? {
    return liveData {
        emit(DBResult.Querying)
        try {
            val res = apiCall.invoke()
            subscribeDBCall(res)
        } catch (t: Throwable) {
            emit(DBResult.DBError(t))
        }
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


