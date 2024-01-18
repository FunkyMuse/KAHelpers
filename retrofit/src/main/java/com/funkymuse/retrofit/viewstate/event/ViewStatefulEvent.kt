package com.funkymuse.retrofit.viewstate.event

import okhttp3.ResponseBody


sealed interface ViewStatefulEvent {
    data class Error(val throwable: Throwable) : ViewStatefulEvent
    data class ApiError(val errorBody: ResponseBody?, val responseCode: Int) : ViewStatefulEvent

    data object Success : ViewStatefulEvent
    data object Loading : ViewStatefulEvent
    data object Idle : ViewStatefulEvent
}

val ViewStatefulEvent.isLoading get() = this is ViewStatefulEvent.Loading
val ViewStatefulEvent.isNotLoading get() = this !is ViewStatefulEvent.Loading

val ViewStatefulEvent.isIdle get() = this is ViewStatefulEvent.Idle
val ViewStatefulEvent.isNotIdle get() = this !is ViewStatefulEvent.Idle

val ViewStatefulEvent.isError get() = this is ViewStatefulEvent.Error
val ViewStatefulEvent.isNotError get() = this !is ViewStatefulEvent.Error

val ViewStatefulEvent.isApiError get() = this is ViewStatefulEvent.ApiError
val ViewStatefulEvent.isNotApiError get() = this !is ViewStatefulEvent.ApiError

val ViewStatefulEvent.isErrorOrApiError get() = this is ViewStatefulEvent.Error || this is ViewStatefulEvent.ApiError

val ViewStatefulEvent.isSuccess get() = this is ViewStatefulEvent.Success
val ViewStatefulEvent.isNotSuccess get() = this !is ViewStatefulEvent.Success

val ViewStatefulEvent.asError get() = (this as ViewStatefulEvent.Error)
val ViewStatefulEvent.asErrorThrowable get() = (this as ViewStatefulEvent.Error).throwable
val ViewStatefulEvent.asApiError get() = (this as ViewStatefulEvent.ApiError)
val ViewStatefulEvent.asApiErrorBody get() = (this as ViewStatefulEvent.ApiError).errorBody
val ViewStatefulEvent.asApiErrorCode get() = (this as ViewStatefulEvent.ApiError).responseCode


val ViewStatefulEvent.getAsThrowable: Throwable? get() = if (this is ViewStatefulEvent.Error) throwable else null
val ViewStatefulEvent.getAsApiFailureCode: Int? get() = if (this is ViewStatefulEvent.ApiError) responseCode else null
val ViewStatefulEvent.getAsApiResponseBody: ResponseBody? get() = if (this is ViewStatefulEvent.ApiError) errorBody else null

fun ViewStatefulEvent.asSuccess() = this as ViewStatefulEvent.Success
fun ViewStatefulEvent.asLoading() = this as ViewStatefulEvent.Loading
fun ViewStatefulEvent.asError() = this as ViewStatefulEvent.Error
fun ViewStatefulEvent.asApiError() = this as ViewStatefulEvent.ApiError
fun ViewStatefulEvent.asIdle() = this as ViewStatefulEvent.Idle

inline fun ViewStatefulEvent.onError(action: (Throwable) -> Unit): ViewStatefulEvent {
    if (this is ViewStatefulEvent.Error) {
        action(throwable)
    }
    return this
}

inline fun ViewStatefulEvent.onSuccess(action: () -> Unit): ViewStatefulEvent {
    if (this is ViewStatefulEvent.Success) {
        action()
    }
    return this
}

inline fun ViewStatefulEvent.onIdle(action: () -> Unit): ViewStatefulEvent {
    if (this is ViewStatefulEvent.Idle) {
        action()
    }
    return this
}

inline fun ViewStatefulEvent.onLoading(action: () -> Unit): ViewStatefulEvent {
    if (this is ViewStatefulEvent.Loading) {
        action()
    }
    return this
}

inline fun ViewStatefulEvent.onApiError(action: (errorBody: ResponseBody?, responseCode: Int) -> Unit): ViewStatefulEvent {
    if (this is ViewStatefulEvent.ApiError) {
        action(errorBody, responseCode)
    }
    return this
}

