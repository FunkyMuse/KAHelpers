package com.crazylegend.retrofit.retrofitResult

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.crazylegend.retrofit.errorResponseCodeMessage
import com.crazylegend.retrofit.throwables.NoConnectionException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

inline fun <T> RetrofitResult<T>.handle(
        loading: () -> Unit = {},
        emptyData: () -> Unit = {},
        callError: (throwable: Throwable) -> Unit = { _ -> },
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
            callError(throwable)
        }
        is RetrofitResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }
}

//region State
fun retrofitCallError(throwable: Throwable) = RetrofitResult.Error(throwable)
fun retrofitApiError(code: Int, errorBody: ResponseBody?) = RetrofitResult.ApiError(code, errorBody)
fun <T> retrofitSuccess(value: T) = RetrofitResult.Success(value)
val retrofitLoading get() = RetrofitResult.Loading
val retrofitEmptyData get() = RetrofitResult.EmptyData


fun <T> retrofitSubscribe(response: Response<T>): RetrofitResult<T> {

    if (response.isSuccessful) {
        val body = response.body() ?: return retrofitEmptyData
        return retrofitSuccess(body)
    } else {
        return retrofitApiError(response.code(), response.errorBody())
    }
}


fun <T> retrofitSubscribeList(response: Response<T>, includeEmptyData: Boolean = true): RetrofitResult<T> {
    if (response.isSuccessful) {
        val body = response.body()

        return when {
            body == null -> {
                retrofitEmptyData
            }
            includeEmptyData -> {
                body.isListAndIsNullOrEmpty<T?, RetrofitResult<T>>(actionFalse = {
                    retrofitSubscribe(response)
                }, actionTrue = {
                    retrofitEmptyData
                })
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

fun <T> MutableLiveData<RetrofitResult<T>>.subscribe(response: Response<T>) {
    value = retrofitSubscribe(response)
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribePost(response: Response<T>) {
    postValue(retrofitSubscribe(response))
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribeList(response: Response<T>, includeEmptyData: Boolean = false) {
    value = retrofitSubscribeList(response, includeEmptyData)
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribeListPost(response: Response<T>, includeEmptyData: Boolean = false) {
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
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onLoading(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.Loading -> {
                action()
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
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onEmptyData(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.EmptyData -> {
                action()
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
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onCallError(action: (throwable: Throwable) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Error -> {
                action(it.throwable)
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
        }
    }
}

inline fun <T> LiveData<RetrofitResult<T>>.onApiError(action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                action(it.responseCode, it.errorBody)
            }
        }
    }
}

inline fun <T> MutableLiveData<RetrofitResult<T>>.onApiError(onErrorMessage: String.() -> Unit = {}, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                errorResponseCodeMessage(it.responseCode).onErrorMessage()
                action(it.responseCode, it.errorBody)
            }
        }
    }
}

inline fun <T> LiveData<RetrofitResult<T>>.onApiError(onErrorMessage: String.() -> Unit = {}, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                errorResponseCodeMessage(it.responseCode).onErrorMessage()
                action(it.responseCode, it.errorBody)
            }
        }
    }
}

inline fun <T> RetrofitResult<T>.onLoading(function: () -> Unit = {}) {
    if (this is RetrofitResult.Loading) function()
}

inline fun <T> RetrofitResult<T>.onEmptyData(function: () -> Unit = {}) {
    if (this is RetrofitResult.EmptyData) function()
}

inline fun <T> RetrofitResult<T>.onCallError(function: (throwable: Throwable) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
}

inline fun <T> RetrofitResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> }) {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }
}

inline fun <T> RetrofitResult<T>.onSuccess(function: (model: T) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Success) {
        function(value)
    }
}
//endregion

internal inline fun <T, R> T.isListAndNotNullOrEmpty(actionFalse: () -> R, actionTrue: () -> R): R =
        if (this is List<*> && !this.isNullOrEmpty()) actionTrue() else actionFalse()

internal inline fun <T, R> T.isListAndIsNullOrEmpty(actionFalse: () -> R, actionTrue: () -> R): R =
        if (this is List<*> && this.isNullOrEmpty()) actionTrue() else actionFalse()


fun <T> MutableStateFlow<RetrofitResult<T>>.loading() {
    value = retrofitLoading
}


fun <T> MutableStateFlow<RetrofitResult<T>>.emptyData() {
    value = retrofitEmptyData
}

fun <T> MutableStateFlow<RetrofitResult<T>>.subscribe(response: Response<T>) {
    value = retrofitSubscribe(response)
}

fun <T> MutableStateFlow<RetrofitResult<T>>.subscribeList(response: Response<T>, includeEmptyData: Boolean = false) {
    value = retrofitSubscribeList(response, includeEmptyData)
}

fun <T> MutableStateFlow<RetrofitResult<T>>.callError(throwable: Throwable) {
    value = retrofitCallError(throwable)
}


fun <T> MutableStateFlow<RetrofitResult<T>>.success(model: T) {
    value = retrofitSuccess(model)
}

fun <T> MutableStateFlow<RetrofitResult<T>>.apiError(code: Int, errorBody: ResponseBody?) {
    value = retrofitApiError(code, errorBody)
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.loading() {
    emit(retrofitLoading)
}


suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.emptyData() {
    emit(retrofitEmptyData)
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.subscribe(response: Response<T>) {
    emit(retrofitSubscribe(response))
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.subscribeList(response: Response<T>, includeEmptyData: Boolean = false) {
    emit(retrofitSubscribeList(response, includeEmptyData))
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.callError(throwable: Throwable) {
    emit(retrofitCallError(throwable))
}


suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.success(model: T) {
    emit(retrofitSuccess(model))
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.apiError(code: Int, errorBody: ResponseBody?) {
    emit(retrofitApiError(code, errorBody))
}

fun <T> Response<T>.unwrapResponseToModel(): T? = when {
    isSuccessful -> body()
    else -> null
}


inline fun <T> RetrofitResult<T>.retryWhenInternetIsAvailable(internetDetector: LiveData<Boolean>,
                                                              lifecycleOwner: LifecycleOwner,
                                                              crossinline retry: () -> Unit) {

    if (this is RetrofitResult.Error && throwable is NoConnectionException) {
        retryOnConnectedToInternet(internetDetector, lifecycleOwner, retry)
    }
}

inline fun retryOnConnectedToInternet(internetDetector: LiveData<Boolean>,
                                      lifecycleOwner: LifecycleOwner,
                                      crossinline retry: () -> Unit) {
    internetDetector.observe(lifecycleOwner) {
        if (it) {
            retry()
        }
    }
}
