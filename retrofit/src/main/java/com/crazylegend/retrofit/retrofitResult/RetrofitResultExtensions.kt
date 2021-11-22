package com.crazylegend.retrofit.retrofitResult

import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

fun retrofitCallError(throwable: Throwable) = RetrofitResult.Error(throwable)
fun retrofitApiError(code: Int, errorBody: ResponseBody?) = RetrofitResult.ApiError(code, errorBody)
fun <T> retrofitSuccess(value: T) = RetrofitResult.Success(value)
val retrofitLoading get() = RetrofitResult.Loading
val retrofitIdle get() = RetrofitResult.Idle

fun <T> retrofitSubscribe(response: Response<T>): RetrofitResult<T> =
    response.unwrapResponseToModel()?.let { retrofitSuccess(it) }
        ?: retrofitApiError(response.code(), response.errorBody())


//region State
inline fun <T> RetrofitResult<T>.onLoading(function: () -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Loading) function()

    return this
}

inline fun <T> RetrofitResult<T>.onIdle(function: () -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Idle) function()

    return this
}

inline fun <T> RetrofitResult<T>.onError(function: (throwable: Throwable) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
    return this
}

inline fun <T> RetrofitResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }

    return this
}

inline fun <T> RetrofitResult<T>.onSuccess(function: (model: T) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Success) {
        function(value)
    }
    return this
}
//endregion


//region State suspend
suspend fun <T> RetrofitResult<T>.onLoadingSuspend(function: suspend () -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Loading) function()

    return this
}

suspend fun <T> RetrofitResult<T>.onIdleSuspend(function: suspend () -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Idle) function()

    return this
}

suspend fun <T> RetrofitResult<T>.onErrorSuspend(function: suspend (throwable: Throwable) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
    return this
}

suspend fun <T> RetrofitResult<T>.onApiErrorSuspend(function: suspend (errorBody: ResponseBody?, responseCode: Int) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }

    return this
}

suspend fun <T> RetrofitResult<T>.onSuccessSuspend(function: suspend (model: T) -> Unit): RetrofitResult<T> {
    if (this is RetrofitResult.Success) {
        function(value)
    }
    return this
}
//endregion


fun <VALUE, TRANSFORM> RetrofitResult<VALUE>.transform(transformer: (VALUE) -> TRANSFORM) :TRANSFORM? =
    getAsSuccess?.let { value -> transformer(value) }


fun <T> Response<T>.unwrapResponseToModel(): T? = when {
    isSuccessful -> body()
    else -> null
}

val <T> RetrofitResult<T>.isLoading get() = this is RetrofitResult.Loading
val <T> RetrofitResult<T>.isSuccess get() = this is RetrofitResult.Success
val <T> RetrofitResult<T>.isSuccessAndValueIsListAndNullOrEmpty get() = this is RetrofitResult.Success && isValueAListAndNullOrEmpty
val <T> RetrofitResult<T>.isSuccessAndValueIsListAndNotNullOrEmpty get() = this is RetrofitResult.Success && isValueAListAndNotNullOrEmpty
val <T> RetrofitResult<T>.isIdle get() = this is RetrofitResult.Idle
val <T> RetrofitResult<T>.isApiError get() = this is RetrofitResult.ApiError
val <T> RetrofitResult<T>.isError get() = this is RetrofitResult.Error


val <T> RetrofitResult<T>.getAsSuccess: T? get() = if (this is RetrofitResult.Success) value else null
val <T> RetrofitResult<T>.getAsThrowable: Throwable? get() = if (this is RetrofitResult.Error) throwable else null
val <T> RetrofitResult<T>.getAsApiFailureCode: Int? get() = if (this is RetrofitResult.ApiError) responseCode else null
val <T> RetrofitResult<T>.getAsApiResponseBody: ResponseBody? get() = if (this is RetrofitResult.ApiError) errorBody else null

fun <T> RetrofitResult<T>.asSuccess() = this as RetrofitResult.Success<T>
fun <T> RetrofitResult<T>.asLoading() = this as RetrofitResult.Loading
fun <T> RetrofitResult<T>.asError() = this as RetrofitResult.Error
fun <T> RetrofitResult<T>.asApiError() = this as RetrofitResult.ApiError
fun <T> RetrofitResult<T>.asIdle() = this as RetrofitResult.Idle