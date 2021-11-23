package com.crazylegend.retrofit.viewstate

import okhttp3.ResponseBody

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
sealed interface ViewEvent {
    data class Error(val throwable: Throwable) : ViewEvent
    data class ApiError(val errorBody: ResponseBody?, val code: Int) : ViewEvent

    object Success : ViewEvent
    object Loading : ViewEvent
    object Idle : ViewEvent
}

val ViewEvent.isLoading get() = this is ViewEvent.Loading
val ViewEvent.isIdle get() = this is ViewEvent.Idle
val ViewEvent.isError get() = this is ViewEvent.Error
val ViewEvent.isApiError get() = this is ViewEvent.ApiError
val ViewEvent.isSuccess get() = this is ViewEvent.Success

val ViewEvent.asError get() = (this as ViewEvent.Error)
val ViewEvent.asErrorThrowable get() = (this as ViewEvent.Error).throwable
val ViewEvent.asApiError get() = (this as ViewEvent.ApiError)
val ViewEvent.asApiErrorBody get() = (this as ViewEvent.ApiError).errorBody
val ViewEvent.asApiErrorCode get() = (this as ViewEvent.ApiError).code

fun ViewEvent.onError(action: (Throwable) -> Unit): ViewEvent {
    if (this is ViewEvent.Error) {
        action(throwable)
    }
    return this
}

fun ViewEvent.onSuccess(action: () -> Unit): ViewEvent {
    if (this is ViewEvent.Success) {
        action()
    }
    return this
}

fun ViewEvent.onIdle(action: () -> Unit): ViewEvent {
    if (this is ViewEvent.Idle) {
        action()
    }
    return this
}

fun ViewEvent.onLoading(action: () -> Unit): ViewEvent {
    if (this is ViewEvent.Loading) {
        action()
    }
    return this
}

fun ViewEvent.onApiError(action: (errorBody: ResponseBody?, responseCode: Int) -> Unit): ViewEvent {
    if (this is ViewEvent.ApiError) {
        action(errorBody, code)
    }
    return this
}

