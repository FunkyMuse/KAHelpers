package com.funkymuse.retrofit.viewstate.state

import androidx.lifecycle.SavedStateHandle
import com.funkymuse.retrofit.apiresult.ApiResult
import com.funkymuse.retrofit.apiresult.onSuccess
import com.funkymuse.retrofit.throwables.isNoConnectionException
import com.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import com.funkymuse.retrofit.viewstate.event.asError
import com.funkymuse.retrofit.viewstate.event.isApiError
import com.funkymuse.retrofit.viewstate.event.isError
import com.funkymuse.retrofit.viewstate.event.isLoading
import com.funkymuse.retrofit.viewstate.event.isSuccess
import okhttp3.ResponseBody


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


private const val errorStateKey = "viewstate.state.errorStateKey.errorJSONKeyApiResult"

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
        val retrofitResult = viewState.value
        return isDataNotLoaded && (retrofitResult.isError or retrofitResult.isApiError or retrofitResult.isSuccess)
    }

val <T> ViewStateContract<T>.showEmptyDataOnErrors: Boolean
    get() {
        val retrofitResult = viewState.value
        return isDataNotLoaded && (retrofitResult.isError or retrofitResult.isApiError)
    }

val <T> ViewStateContract<T>.showEmptyDataOnApiError: Boolean
    get() {
        val retrofitResult = viewState.value
        return isDataNotLoaded && (retrofitResult.isApiError)
    }


val <T> ViewStateContract<T>.showEmptyDataOnError: Boolean
    get() {
        val retrofitResult = viewState.value
        return isDataNotLoaded && (retrofitResult.isError)
    }

val <T> ViewStateContract<T>.showEmptyDataOnSuccess: Boolean
    get() {
        val retrofitResult = viewState.value
        return isDataNotLoaded && retrofitResult.isSuccess
    }

val <T> ViewStateContract<T>.showEmptyDataOnNoConnection: Boolean
    get() {
        val retrofitResult = viewState.value
        return isDataNotLoaded &&
                retrofitResult.isError &&
                retrofitResult.asError().throwable.isNoConnectionException
    }

val <T> ViewStateContract<T>.showLoadingWhenDataNotLoaded: Boolean
    get() {
        val retrofitResult = viewState.value
        return retrofitResult.isLoading and isDataNotLoaded
    }

val <T> ViewStateContract<T>.showLoadingWhenDataIsLoaded: Boolean
    get() {
        val retrofitResult = viewState.value
        return retrofitResult.isLoading and isDataLoaded
    }


fun <T> ApiResult<T>.asViewEvent() = when (this) {
    is ApiResult.ApiError -> ViewStatefulEvent.ApiError(errorBody, responseCode)
    is ApiResult.Error -> ViewStatefulEvent.Error(throwable)
    ApiResult.Idle -> ViewStatefulEvent.Idle
    ApiResult.Loading -> ViewStatefulEvent.Loading
    is ApiResult.Success -> ViewStatefulEvent.Success
}

fun <T> ViewStateContract<T>.startLoading() = ApiResult.Loading.asViewStatePayload(this)
suspend fun <T> ViewStateContract<T>.startLoadingWithEvents() = ApiResult.Loading.asViewStatePayloadWithEvents(this)