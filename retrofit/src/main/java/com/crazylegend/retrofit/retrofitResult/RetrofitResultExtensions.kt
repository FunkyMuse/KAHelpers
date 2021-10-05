package com.crazylegend.retrofit.retrofitResult

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.crazylegend.retrofit.retryOnConnectedToInternet
import com.crazylegend.retrofit.throwables.NoConnectionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

//region State
fun retrofitCallError(throwable: Throwable) = RetrofitResult.Error(throwable)
fun retrofitApiError(code: Int, errorBody: ResponseBody?) = RetrofitResult.ApiError(code, errorBody)
fun <T> retrofitSuccess(value: T) = RetrofitResult.Success(value)
val retrofitLoading get() = RetrofitResult.Loading
val retrofitIdle get() = RetrofitResult.Loading

fun <T> retrofitSubscribe(response: Response<T>): RetrofitResult<T> = response.unwrapResponseToModel()?.let { retrofitSuccess(it) } ?: retrofitApiError(response.code(), response.errorBody())

inline fun <T> RetrofitResult<T>.onLoading(function: () -> Unit = {}): RetrofitResult<T> {
    if (this is RetrofitResult.Loading) function()

    return this
}

inline fun <T> RetrofitResult<T>.onIdle(function: () -> Unit = {}): RetrofitResult<T> {
    if (this is RetrofitResult.Idle) function()

    return this
}

inline fun <T> RetrofitResult<T>.onCallError(function: (throwable: Throwable) -> Unit = { _ -> }): RetrofitResult<T> {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
    return this
}

inline fun <T> RetrofitResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> }): RetrofitResult<T> {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }

    return this
}

inline fun <T> RetrofitResult<T>.onSuccess(function: (model: T) -> Unit = { _ -> }): RetrofitResult<T> {
    if (this is RetrofitResult.Success) {
        function(value)
    }
    return this
}
//endregion


fun <T> MutableStateFlow<RetrofitResult<T>>.loading() {
    value = retrofitLoading
}


fun <T> MutableStateFlow<RetrofitResult<T>>.idle() {
    value = retrofitIdle
}

fun <T> MutableStateFlow<RetrofitResult<T>>.subscribe(response: Response<T>) {
    value = retrofitSubscribe(response)
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


suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.idle() {
    emit(retrofitLoading)
}

suspend fun <T> MutableSharedFlow<RetrofitResult<T>>.subscribe(response: Response<T>) {
    emit(retrofitSubscribe(response))
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


inline fun <T> RetrofitResult<T>.retryWhenInternetIsAvailable(
    internetDetector: LiveData<Boolean>,
    lifecycleOwner: LifecycleOwner,
    crossinline retry: () -> Unit
) {

    if (this is RetrofitResult.Error && throwable is NoConnectionException) {
        retryOnConnectedToInternet(internetDetector, lifecycleOwner, retry)
    }
}

inline fun <T> RetrofitResult<T>.retryWhenInternetIsAvailable(
    internetDetector: Flow<Boolean>,
    coroutineScope: CoroutineScope,
    crossinline retry: () -> Unit
) {

    if (this is RetrofitResult.Error && throwable is NoConnectionException) {
        retryOnConnectedToInternet(internetDetector, coroutineScope, retry)
    }
}