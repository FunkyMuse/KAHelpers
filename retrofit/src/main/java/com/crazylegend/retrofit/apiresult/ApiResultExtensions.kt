package com.crazylegend.retrofit.apiresult

import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

fun apiCallError(throwable: Throwable) = ApiResult.Error(throwable)
fun apiError(code: Int, errorBody: ResponseBody?) = ApiResult.ApiError(code, errorBody)
fun <T> apiSuccess(value: T) = ApiResult.Success(value)
val apiLoading get() = ApiResult.Loading
val apiIdle get() = ApiResult.Idle

fun <T> apiResultSubscribe(response: Response<T>): ApiResult<T> =
        response.unwrapResponseToModel()?.let { apiSuccess(it) }
                ?: apiError(response.code(), response.errorBody())


//region State
inline fun <T> ApiResult<T>.onLoading(function: () -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) function()

    return this
}

inline fun <T> ApiResult<T>.onIdle(function: () -> Unit): ApiResult<T> {
    if (this is ApiResult.Idle) function()

    return this
}

inline fun <T> ApiResult<T>.onError(function: (throwable: Throwable) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        function(throwable)
    }
    return this
}

inline fun <T> ApiResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit): ApiResult<T> {
    if (this is ApiResult.ApiError) {
        function(errorBody, responseCode)
    }

    return this
}

inline fun <T> ApiResult<T>.onSuccess(function: (model: T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        function(value)
    }
    return this
}
//endregion


//region State suspend
suspend fun <T> ApiResult<T>.onLoadingSuspend(function: suspend () -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) function()

    return this
}

suspend fun <T> ApiResult<T>.onIdleSuspend(function: suspend () -> Unit): ApiResult<T> {
    if (this is ApiResult.Idle) function()

    return this
}

suspend fun <T> ApiResult<T>.onErrorSuspend(function: suspend (throwable: Throwable) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        function(throwable)
    }
    return this
}

suspend fun <T> ApiResult<T>.onApiErrorSuspend(function: suspend (errorBody: ResponseBody?, responseCode: Int) -> Unit): ApiResult<T> {
    if (this is ApiResult.ApiError) {
        function(errorBody, responseCode)
    }

    return this
}

suspend fun <T> ApiResult<T>.onSuccessSuspend(function: suspend (model: T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        function(value)
    }
    return this
}
//endregion


fun <VALUE, TRANSFORM> ApiResult<VALUE>.transformAsSuccess(transformer: (VALUE) -> TRANSFORM): TRANSFORM? =
        getAsSuccess?.let { value -> transformer(value) }

fun <VALUE, TRANSFORM> ApiResult<VALUE>.transform(transformer: (VALUE) -> TRANSFORM): ApiResult<TRANSFORM> =
        if (this is ApiResult.Success) {
            ApiResult.Success(transformer(value))
        } else {
            this as ApiResult<TRANSFORM>
        }


fun <T> Response<T>.unwrapResponseToModel(): T? = when {
    isSuccessful -> body()
    else -> null
}

val <T> ApiResult<T>.isLoading get() = this is ApiResult.Loading
val <T> ApiResult<T>.isSuccess get() = this is ApiResult.Success
val <T> ApiResult<T>.isSuccessAndValueIsListAndNullOrEmpty get() = this is ApiResult.Success && isValueAListAndNullOrEmpty
val <T> ApiResult<T>.isSuccessAndValueIsListAndNotNullOrEmpty get() = this is ApiResult.Success && isValueAListAndNotNullOrEmpty
val <T> ApiResult<T>.isIdle get() = this is ApiResult.Idle
val <T> ApiResult<T>.isApiError get() = this is ApiResult.ApiError
val <T> ApiResult<T>.isError get() = this is ApiResult.Error


val <T> ApiResult<T>.getAsSuccess: T? get() = if (this is ApiResult.Success) value else null
val <T> ApiResult<T>.getAsThrowable: Throwable? get() = if (this is ApiResult.Error) throwable else null
val <T> ApiResult<T>.getAsApiFailureCode: Int? get() = if (this is ApiResult.ApiError) responseCode else null
val <T> ApiResult<T>.getAsApiResponseBody: ResponseBody? get() = if (this is ApiResult.ApiError) errorBody else null

fun <T> ApiResult<T>.asSuccess() = this as ApiResult.Success<T>
fun <T> ApiResult<T>.asLoading() = this as ApiResult.Loading
fun <T> ApiResult<T>.asError() = this as ApiResult.Error
fun <T> ApiResult<T>.asApiError() = this as ApiResult.ApiError
fun <T> ApiResult<T>.asIdle() = this as ApiResult.Idle