package com.crazylegend.retrofit.viewstate.state

import androidx.lifecycle.SavedStateHandle
import com.crazylegend.retrofit.apiresult.*
import com.crazylegend.retrofit.throwables.isNoConnectionException
import com.crazylegend.retrofit.viewstate.event.ViewEvent
import okhttp3.ResponseBody

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */


fun <T> ApiResult<T>.asViewStatePayload(viewState: ViewStateContract<T>): ApiResult<T> {
    onSuccess {
        viewState.payload = it
    }
    viewState.emitState(this)
    return this
}


fun <T> ViewStateContract<T>.fromRetrofit(apiResult: ApiResult<T>): ViewStateContract<T> {
    apiResult.onSuccess {
        payload = it
    }
    emitState(apiResult)
    return this
}

suspend fun <T> ApiResult<T>.asViewStatePayloadWithEvents(viewState: ViewStateContract<T>): ApiResult<T> {
    onSuccess {
        viewState.payload = it
    }
    viewState.emitState(this)
    viewState.emitEvent(this)
    return this
}


suspend fun <T> ViewStateContract<T>.fromRetrofitWithEvents(apiResult: ApiResult<T>): ViewStateContract<T> {
    apiResult.onSuccess {
        payload = it
    }
    emitState(apiResult)
    emitEvent(apiResult)
    return this
}


private const val errorStateKey = "errorJSONKeyApiResult"

fun SavedStateHandle.handleApiErrorFromSavedState(errorBody: ResponseBody?): String? {
    val json = errorBody?.string()
    if (json.isNullOrEmpty()) return get<String>(errorStateKey)
    this[errorStateKey] = json

    return get<String>(errorStateKey)
}

fun handleApiError(savedStateHandle: SavedStateHandle, errorBody: ResponseBody?): String? =
    savedStateHandle.handleApiErrorFromSavedState(errorBody)


val <T> ViewStateContract<T>.showEmptyDataOnErrorsOrSuccess: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded && (retrofitResult.isError or retrofitResult.isApiError or retrofitResult.isSuccess)
    }

val <T> ViewStateContract<T>.showEmptyDataOnErrors: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded && (retrofitResult.isError or retrofitResult.isApiError)
    }

val <T> ViewStateContract<T>.showEmptyDataOnApiError: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded && (retrofitResult.isApiError)
    }


val <T> ViewStateContract<T>.showEmptyDataOnError: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded && (retrofitResult.isError)
    }

val <T> ViewStateContract<T>.showEmptyDataOnSuccess: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded && retrofitResult.isSuccess
    }

val <T> ViewStateContract<T>.shouldShowEmptyDataOnNoConnection: Boolean
    get() {
        val retrofitResult = data.value
        return isDataNotLoaded &&
                retrofitResult.isError &&
                retrofitResult.asError().throwable.isNoConnectionException
    }


fun <T> ApiResult<T>.asViewEvent() = when (this) {
    is ApiResult.ApiError -> ViewEvent.ApiError(errorBody, responseCode)
    is ApiResult.Error -> ViewEvent.Error(throwable)
    ApiResult.Idle -> ViewEvent.Idle
    ApiResult.Loading -> ViewEvent.Loading
    is ApiResult.Success -> ViewEvent.Success
}