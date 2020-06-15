package com.crazylegend.kotlinextensions.retrofit.retrofitResult

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.collections.isListAndNotNullOrEmpty
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.retrofit.errorResponseCode
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

inline fun <T> RetrofitResult<T>.handle(
        loading: () -> Unit,
        emptyData: () -> Unit,
        calError: (throwable: Throwable) -> Unit = { _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: T.() -> Unit
) {

    when (this) {
        is RetrofitResult.Success -> {
            success.invoke(value)
        }
        RetrofitResult.Loading -> {
            loading()
        }
        RetrofitResult.EmptyData -> {
            emptyData()
        }
        is RetrofitResult.Error -> {
            calError(throwable)
        }
        is RetrofitResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }.exhaustive
}

//region State
fun retrofitCallError(throwable: Throwable) = RetrofitResult.Error(throwable)
fun retrofitApiError(code: Int, errorBody: ResponseBody?) = RetrofitResult.ApiError(code, errorBody)
fun <T> retrofitSuccess(value: T) = RetrofitResult.Success(value)
val retrofitLoading get() = RetrofitResult.Loading
val retrofitEmptyData get() = RetrofitResult.EmptyData


fun <T> retrofitSubscribe(response: Response<T>?): RetrofitResult<T> {
    if (response == null) return retrofitEmptyData

    if (response.isSuccessful) {
        val body = response.body() ?: return retrofitEmptyData
        return retrofitSuccess(body)
    } else {
        return retrofitApiError(response.code(), response.errorBody())
    }
}


fun <T> retrofitSubscribeList(response: Response<T>?, includeEmptyData: Boolean = false): RetrofitResult<T> {
    if (response == null) return retrofitEmptyData
    if (response.isSuccessful) {
        val body = response.body()

        return when {
            body == null -> {
                retrofitEmptyData
            }
            includeEmptyData -> {
                return if (includeEmptyData) {
                    return body.isListAndNotNullOrEmpty<T?, RetrofitResult<T>>(actionFalse = {
                        retrofitEmptyData
                    }, actionTrue = {
                        retrofitSubscribe(response)
                    })
                } else {
                    retrofitSubscribe(response)
                }
            }
            else -> {
                retrofitSubscribe(response)
            }
        }
    } else {
        return retrofitApiError(response.code(), response.errorBody())
    }

}


fun <T> MutableLiveData<RetrofitResult<T>>.loading() {
    value = retrofitLoading
}

fun <T> MutableLiveData<RetrofitResult<T>>.emptyData() {
    value = retrofitEmptyData
}


fun <T> MutableLiveData<RetrofitResult<T>>.loadingPost() {
    postValue(retrofitLoading)
}

fun <T> MutableLiveData<RetrofitResult<T>>.emptyDataPost() {
    postValue(retrofitEmptyData)
}


inline fun <reified T> isGenericInstanceOf(obj: Any): Boolean = obj is T


fun <T> MutableLiveData<RetrofitResult<T>>.subscribe(response: Response<T>?) {
    value = retrofitSubscribe(response)
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribePost(response: Response<T>?) {
    postValue(retrofitSubscribe(response))
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribeList(response: Response<T>?, includeEmptyData: Boolean = false) {
    value = retrofitSubscribeList(response, includeEmptyData)
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribeListPost(response: Response<T>?, includeEmptyData: Boolean = false) {
    postValue(retrofitSubscribeList(response, includeEmptyData))
}

fun <T> MutableLiveData<RetrofitResult<T>>.callError(throwable: Throwable) {
    value = retrofitCallError(throwable)
}

fun <T> MutableLiveData<RetrofitResult<T>>.callErrorPost(throwable: Throwable) {
    postValue(retrofitCallError(throwable))
}

fun <T> MutableLiveData<RetrofitResult<T>>.success(model: T) {
    value = retrofitSuccess(model)
}

fun <T> MutableLiveData<RetrofitResult<T>>.successPost(model: T) {
    postValue(retrofitSuccess(model))
}

fun <T> MutableLiveData<RetrofitResult<T>>.apiError(code: Int, errorBody: ResponseBody?) {
    value = retrofitApiError(code, errorBody)
}


fun <T> MutableLiveData<RetrofitResult<T>>.apiErrorPost(code: Int, errorBody: ResponseBody?) {
    postValue(retrofitApiError(code, errorBody))
}

//success

fun <T> MutableLiveData<RetrofitResult<T>>.onSuccess(action: (model: T) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Success -> {
                action(it.value)
            }
            else -> {
            }
        }
    }
}

val <T> MutableLiveData<RetrofitResult<T>>.getSuccess: T?
    get() {
        return value?.let {
            when (it) {
                is RetrofitResult.Success -> {
                    it.value
                }
                else -> {
                    null
                }
            }
        }
    }

val <T> LiveData<RetrofitResult<T>>.getSuccess: T?
    get() {
        return value?.let {
            when (it) {
                is RetrofitResult.Success -> {
                    it.value
                }
                else -> {
                    null
                }
            }
        }
    }

//Loading


fun <T> MutableLiveData<RetrofitResult<T>>.onLoading(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.Loading -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onLoading(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.Loading -> {
                action()
            }
            else -> {
            }
        }
    }
}


// Empty data


fun <T> MutableLiveData<RetrofitResult<T>>.onEmptyData(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.EmptyData -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onEmptyData(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.EmptyData -> {
                action()
            }
            else -> {
            }
        }
    }
}

// on call error on your side


fun <T> MutableLiveData<RetrofitResult<T>>.onCallError(action: (throwable: Throwable) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Error -> {
                action(it.throwable)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onCallError(action: (throwable: Throwable) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Error -> {
                action(it.throwable)
            }
            else -> {
            }
        }
    }
}

// on api error on server side


fun <T> MutableLiveData<RetrofitResult<T>>.onApiError(action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onApiError(action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> MutableLiveData<RetrofitResult<T>>.onApiError(context: Context, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                context.errorResponseCode(it.responseCode)
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onApiError(context: Context, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                context.errorResponseCode(it.responseCode)
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> RetrofitResult<T>.onLoading(function: () -> Unit = {}) {
    if (this is RetrofitResult.Loading) function()
}

fun <T> RetrofitResult<T>.onEmptyData(function: () -> Unit = {}) {
    if (this is RetrofitResult.EmptyData) function()
}

fun <T> RetrofitResult<T>.onCallError(function: (throwable: Throwable) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
}

fun <T> RetrofitResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> }) {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }
}

fun <T> RetrofitResult<T>.onSuccess(function: (model: T) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Success) {
        function(value)
    }
}
//endregion
